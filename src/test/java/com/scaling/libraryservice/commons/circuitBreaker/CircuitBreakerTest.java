package com.scaling.libraryservice.commons.circuitBreaker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CircuitBreakerTest {

    @InjectMocks
    private CircuitBreaker circuitBreaker;
    private WireMockServer mockServer;
    @Mock
    private ScheduledExecutorService scheduler;
    @Mock
    private RestorationChecker checker;

    @Mock
    private ScheduledFuture<?> scheduledFuture;

    @Mock
    private Map<ApiObserver, ScheduledFuture<?>> scheduledTasks;
    private ApiObserver apiObserver1;
    private ApiObserver apiObserver2;
    private final String mockUri1 = "http://mockServer.kr/api/bookExist";

    private final String mockUri2 = "http://mockServer.kr/api/loanItem";

    @BeforeEach
    public void setUp() {
        this.apiObserver1 = setApiObserver();
        this.apiObserver2 = setApiObserver();

        mockServer = new WireMockServer(8089);

        mockServer.stubFor(
            WireMock.get("/api/bookExist").willReturn(WireMock.ok()));

        circuitBreaker = new CircuitBreaker(scheduler, scheduledTasks, checker);
    }

    public ApiObserver setApiObserver() {

        ApiStatus apiStatus = new ApiStatus(mockUri1, 10);

        return new ApiObserver() {
            @Override
            public ApiStatus getApiStatus() {
                return apiStatus;
            }
        };
    }



    @Test
    void isClosed() {
        /* given */
        int errorCnt = 20;

        /* when */

        for (int i = 0; i < errorCnt; i++) {
            circuitBreaker.receiveError(apiObserver1);
        }
        /* then */

        assertFalse(circuitBreaker.isApiAccessible(apiObserver1));
    }


    @Test
    @DisplayName("서킷 브레이커 error monitoring")
    public void receiveError() {
        /* given */

        int errorCnt = 10;

        /* when */

        for (int i = 0; i < errorCnt; i++) {
            circuitBreaker.receiveError(apiObserver1);
        }

        /* then */
        assertEquals(errorCnt, apiObserver1.getApiStatus().getErrorCnt());
    }

    @Test
    @DisplayName("apiObserver 간 독립성 보장")
    public void receiveError_independent() {
        /* given */
        int errorCnt = 10;

        /* when */

        for (int i = 0; i < errorCnt; i++) {
            circuitBreaker.receiveError(apiObserver1);
        }

        /* then */

        assertEquals(0, apiObserver2.getApiStatus().getErrorCnt());
    }

    @Test
    @DisplayName("error monitor에 동시성 이슈가 일어나 error cnt 변화에 문제가 생기지 않는다")
    public void receiveError_concurrency() throws InterruptedException {
        /* given */
        int errorCnt = 100;
        ExecutorService executor = Executors.newFixedThreadPool(5);

        /* when */
        Runnable runnable = () -> {
            circuitBreaker.receiveError(apiObserver1);
        };

        for (int i = 0; i < errorCnt; i++) {

            executor.execute(runnable);
        }

        /* then */

        executor.shutdown();
        if (executor.awaitTermination(60, TimeUnit.SECONDS))
            /* then */ {
            assertEquals(apiObserver1.getApiStatus().DEFAULT_MAX_ERROR_CNT,
                apiObserver1.getApiStatus().getErrorCnt());
        }
    }

    @Test
    void startScheduledRestoration() {
        /* given */

        /* when */

        circuitBreaker.startScheduledRestoration(apiObserver1, 1000 * 2, TimeUnit.MILLISECONDS);

        /* then */
        verify(scheduledTasks, times(1)).put(any(), any());
    }

    @Test
    @DisplayName("스케쥴 된 시간에 맞춰 API 상태를 체크")
    void startScheduledRestoration2() throws InterruptedException {
        /* given */

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Long> periodCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> timeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);

        when(scheduler.scheduleAtFixedRate(runnableCaptor.capture(), anyLong(),
            periodCaptor.capture(), timeUnitCaptor.capture()))
            .thenReturn(mock(ScheduledFuture.class));


        /* when */

        circuitBreaker.startScheduledRestoration(apiObserver1, 1000 * 2, TimeUnit.MILLISECONDS);

        runnableCaptor.getValue().run();

        /* then */

        verify(checker, times(1)).isRestoration(any());
    }

    @Test
    @DisplayName("스케쥴 된 시간에 맞춰 API 상태를 체크 했더니 정상")
    void startScheduledRestoration_normal() throws InterruptedException {
        /* given */

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Long> periodCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> timeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);

        when(scheduler.scheduleAtFixedRate(runnableCaptor.capture(), anyLong(),
            periodCaptor.capture(), timeUnitCaptor.capture()))
            .thenReturn(mock(ScheduledFuture.class));

        when(checker.isRestoration(any())).thenReturn(true);

        when(scheduledTasks.get(any())).thenReturn(mock(ScheduledFuture.class));

        /* when */

        circuitBreaker.startScheduledRestoration(apiObserver1, 1000 * 2, TimeUnit.MILLISECONDS);

        runnableCaptor.getValue().run();

        /* then */
        assertTrue(apiObserver1.getApiStatus().apiAccessible());
    }

    @Test
    @DisplayName("에러 임계치를 넘으면 startScheduledRestoration가 호출 된다.")
    void startScheduledRestoration_receiveError() {
        /* given */
        int errorCnt = 20;

        for (int i = 0; i < errorCnt; i++) {
            circuitBreaker.receiveError(apiObserver1);
        }

        /* when */
        var result = circuitBreaker.getScheduledTasks().get(apiObserver1);

        /* then */

        verify(scheduledTasks, times(1)).put(any(), any());
    }


    @Test
    void stopScheduledRestoration() {
        /* given */

        var future = mock(ScheduledFuture.class);

        when(scheduledTasks.get(apiObserver1)).thenReturn(future);

        /* when */
        circuitBreaker.stopScheduledRestoration(apiObserver1);

        /* then */

        verify(future, times(1)).cancel(false);
        verify(scheduledTasks, times(1)).remove(apiObserver1);
        assertTrue(apiObserver1.getApiStatus().apiAccessible());
    }
}