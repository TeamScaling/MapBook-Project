package com.scaling.libraryservice.commons.circuitBreaker;

import com.scaling.libraryservice.commons.apiConnection.ApiStatus;
import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.mapBook.util.ApiQuerySender;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@link CircuitBreaker}는 API 호출에 대한 오류를 관리하고 불안정한 API 서버를 격리합니다. 이 클래스는 API 서버의 상태를 모니터링하고, 연속된 오류
 * 발생 시 API 서버에 대한 액세스를 일시 중단한 다음, 정기적으로 해당 API 서버의 가용성을 확인하여 다시 사용할 수 있는지 여부를 결정합니다.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CircuitBreaker {

    private static final List<ApiObserver> observingConnections = new ArrayList<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Map<ApiStatus, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    private final ApiQuerySender apiQuerySender;

    public boolean checkIsAvailable(ApiStatus apiStatus) {

        var response = apiQuerySender.sendSingleQuery(
            target -> UriComponentsBuilder.fromHttpUrl(apiStatus.getApiUri()), "");

        return response.getStatusCode().is2xxSuccessful();
    }

    /**
     * API 오류가 발생했을 때, 관련 {@link ApiObserver}를 처리하고 오류 횟수를 증가시키는 메소드입니다. 연속된 오류 발생 시
     * {@link CircuitBreaker}를 사용하여 API 서버에 대한 액세스를 일시 중단합니다.
     *
     * @param obj 발생한 오류를 처리할 {@link ApiObserver} 인스턴스
     */
    public synchronized void receiveError(Object obj) {

        Objects.requireNonNull(obj);

        if(obj instanceof ApiObserver observer){

            ApiStatus status = observer.getApiStatus();

            if (!observingConnections.contains(observer)) {
                observingConnections.add(observer);
            }

            status.upErrorCnt();

            if (status.getErrorCnt() > status.DEFAULT_MAX_ERROR_CNT) {
                closeObserver(status);
            }

            log.error("Api Error - request api url : [{}] , current error cnt : [{}]"
                , status.getApiUri(), status.getErrorCnt());
        }

    }

    /**
     * 주어진 {@link ApiStatus}에 해당하는 API 서버를 CircuitBreaker에 의해 일시 중단한 후, 정기적으로 해당 API 서버의 가용성을 확인하여
     * 다시 사용할 수 있는지 여부를 결정하는 메소드입니다.
     *
     * @param status 일시 중단할 API 서버의 상태 정보
     */
    public void closeObserver(ApiStatus status) {

        status.closeAccess();
        log.info(status.getApiUri() + " is closed by nested api server error at [{}]",
            status.getClosedTime());

        Runnable checkAvailabilityTask = () -> {
            if (checkIsAvailable(status)) {
                scheduledTasks.get(status).cancel(false);
                scheduledTasks.remove(status);
                status.openAccess();
            }
        };

        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(checkAvailabilityTask, 1,
            1, TimeUnit.HOURS);
        scheduledTasks.put(status, scheduledFuture);
    }

}
