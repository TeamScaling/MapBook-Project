package com.scaling.libraryservice.commons.circuitBreaker;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

/**
 * {@link CircuitBreaker}는 API 호출에 대한 오류를 관리하고 불안정한 API 서버를 격리합니다. 이 클래스는 API 서버의 상태를 모니터링하고, 연속된 오류
 * 발생 시 API 서버에 대한 액세스를 일시 중단한 다음, 정기적으로 해당 API 서버의 가용성을 확인하여 다시 사용할 수 있는지 여부를 결정합니다.
 */
@Slf4j
@RequiredArgsConstructor
public class CircuitBreaker {
    private final ScheduledExecutorService scheduler;
    private final Map<ApiObserver, ScheduledFuture<?>> scheduledTasks;
    private final RestorationChecker checker;


    /**
     * {@link ApiObserver} 인스턴스의 API 액세스 상태를 확인하는 메소드입니다. {@link ApiObserver}가 현재 접근 가능한 API를 가리키고
     * 있는지 반환합니다.
     *
     * @param apiObserver API 액세스 상태를 확인할 {@link ApiObserver} 인스턴스
     * @return {@link ApiObserver}가 가리키는 API가 접근 가능한 경우 true, 그렇지 않은 경우 false를 반환
     */
    boolean isApiAccessible(@NonNull ApiObserver apiObserver) {
        return apiObserver.getApiStatus().apiAccessible();
    }

    /**
     * API 오류가 발생했을 때, 관련 {@link ApiObserver}를 처리하고 오류 횟수를 증가시키는 메소드입니다. 연속된 오류 발생 시
     * {@link CircuitBreaker}를 사용하여 API 서버에 대한 액세스를 일시 중단합니다.
     *
     * @param observer 발생한 오류를 처리할 {@link ApiObserver} 인스턴스
     */
    synchronized void receiveError(@NonNull ApiObserver observer) {

        ApiStatus status = observer.getApiStatus();

        if (status.apiAccessible()){
            status.upErrorCnt();

            if (status.isMaxError()) closeApi(observer);

            log.error("Api Error - request api url : [{}] , current error cnt : [{}]"
                , status.getApiUri(), status.getErrorCnt());
        }
    }

    /**
     * 주어진 {@link ApiStatus}에 해당하는 API 서버를 CircuitBreaker에 의해 일시 중단한 후, 정기적으로 해당 API 서버의 가용성을 확인하여
     * 다시 사용할 수 있는지 여부를 결정하는 메소드입니다.
     *
     * @param observer 일시 중단할 {@link ApiObserver} 인스턴스
     */
    private synchronized void closeApi(ApiObserver observer) {

        ApiStatus status = observer.getApiStatus();

        status.closeAccess();
        log.info(status.getApiUri() + " is closed by nested api server error at [{}]",
            status.getClosedTime());

        startScheduledRestoration(observer,60*30,TimeUnit.SECONDS);
    }

    void startScheduledRestoration(ApiObserver observer,int period,TimeUnit timeUnit){

        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
                if (checker.isRestoration(observer)) {
                    stopScheduledRestoration(observer);
                }}, 1, period, timeUnit);

        scheduledTasks.put(observer, scheduledFuture);
    }

    void stopScheduledRestoration(ApiObserver observer){

        scheduledTasks.get(observer).cancel(false);
        scheduledTasks.remove(observer);
        observer.getApiStatus().openAccess();
    }

    public Map<ApiObserver, ScheduledFuture<?>> getScheduledTasks() {
        return scheduledTasks;
    }
}
