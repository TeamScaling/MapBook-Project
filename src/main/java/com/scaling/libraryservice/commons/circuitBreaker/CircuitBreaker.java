package com.scaling.libraryservice.commons.circuitBreaker;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.scaling.libraryservice.commons.circuitBreaker.restoration.RestorationChecker;
import com.scaling.libraryservice.logging.logger.OpenApiLogger;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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
    private static final int INITIAL_DELAY = 1;
    private static final int RECOVERY_INTERVAL = 60 * 30;
    private final OpenApiLogger openApiLogger;

    /**
     * {@link ApiObserver} 인스턴스의 API 액세스 상태를 확인하는 메소드입니다. {@link ApiObserver}가 현재 접근 가능한 API를 가리키고
     * 있는지 반환합니다.
     *
     * @param apiObserver API 액세스 상태를 확인할 {@link ApiObserver} 인스턴스
     * @return {@link ApiObserver}가 가리키는 API가 접근 가능한 경우 true, 그렇지 않은 경우 false를 반환
     */
    public boolean isApiAccessible(@NonNull ApiObserver apiObserver) {
        return apiObserver.getApiStatus().apiAccessible();
    }

    /**
     * API 오류가 발생했을 때, 관련 {@link ApiObserver}를 처리하고 오류 횟수를 증가시키는 메소드입니다. 연속된 오류 발생 시
     * {@link CircuitBreaker}를 사용하여 API 서버에 대한 액세스를 일시 중단합니다.
     *
     * @param observer 발생한 오류를 처리할 {@link ApiObserver} 인스턴스
     */
    public synchronized void receiveError(@NonNull ApiObserver observer) {

        ApiStatus status = observer.getApiStatus();
        status.upErrorCnt();

        if (status.apiAccessible() && status.isMaxError()) {
            closeApi(observer);
            openApiLogger.sendLogToSlack(status);
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

        startScheduledRestoration(observer);
    }

    /**
     * 주어진 {@link ApiObserver}에 대해 주기적인 복구 작업을 스케줄링하는 메소드입니다. 이 메소드는 {@link ApiObserver}가 가리키는 API
     * 서버의 가용성을 정기적으로 확인합니다. 복구 가능한 경우 (즉, API 서버가 다시 사용 가능해질 경우), 스케줄링된 복구 작업을 중지합니다.
     *
     * @param observer 복구 작업을 스케줄링할 {@link ApiObserver} 인스턴스
     */
    void startScheduledRestoration(ApiObserver observer) {

        scheduledTasks.put(observer, scheduleRestorationTask(observer));
    }

    private ScheduledFuture<?> scheduleRestorationTask(ApiObserver observer) {
        return scheduler.scheduleAtFixedRate(
            createRestorationRunnable(observer),
            INITIAL_DELAY,
            RECOVERY_INTERVAL,
            SECONDS
        );
    }

    private Runnable createRestorationRunnable(ApiObserver observer) {
        return () -> {
            if (checker.isRestoration(observer)) {
                stopScheduledRestoration(observer);
            }
        };
    }

    /**
     * 주어진 {@link ApiObserver}에 대한 스케줄링된 복구 작업을 중지하는 메소드입니다. 이 메소드는 {@link ApiObserver}가 가리키는 API
     * 서버에 대한 접근을 다시 허용합니다.
     *
     * @param observer 복구 작업을 중지할 {@link ApiObserver} 인스턴스
     */
    void stopScheduledRestoration(ApiObserver observer) {

        scheduledTasks.get(observer).cancel(false);
        scheduledTasks.remove(observer);

        observer.getApiStatus().openAccess();
    }

    /**
     * 현재 스케줄링된 복구 작업을 모두 반환하는 메소드입니다.
     *
     * @return 각 {@link ApiObserver}에 대한 스케줄링된 복구 작업을 포함하는 맵
     */
    Map<ApiObserver, ScheduledFuture<?>> getScheduledTasks() {
        return scheduledTasks;
    }
}
