package com.scaling.libraryservice.commons.circuitBreaker;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.commons.api.apiConnection.LoanableLibConn;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CircuitBreakerAspectTest {
    @InjectMocks
    private CircuitBreakerAspect circuitBreakerAspect;
    @Mock
    private CircuitBreaker circuitBreaker;
    @Mock
    private CircuitBreakerSupporter support;
    @Mock
    private ProceedingJoinPoint joinPoint;
    @Mock
    private MethodSignature signature;
    @Mock
    private ApiObserver apiObserver;
    private Method originMethod;
    private Method substituteMethod;

    @BeforeEach
    public void setUP() throws NoSuchMethodException {
        circuitBreakerAspect = new CircuitBreakerAspect(circuitBreaker,support);

        originMethod = this.getClass().getDeclaredMethod("targetApiMonitoring");
        substituteMethod = this.getClass().getDeclaredMethod("fallBackMethod");
    }

    @ApiMonitoring(api = LoanableLibConn.class,substitute = "fallBackMethod")
    @DisplayName("테스트를 위한 AOP PointCut method")
    public void targetApiMonitoring(){
        System.out.println("hello!!!!!!!!!!");
    }

    @DisplayName("테스트를 위한 API 장애시 대체 메소드")
    public boolean fallBackMethod(){

        return true;
    }

    @Test
    void apiMonitoringPointcut() {
        circuitBreakerAspect.apiMonitoringPointcut();
    }


    @Test @DisplayName("API 장애 상태 일때, 대체 메소드로 전환 할 수 있다.")
    public void apiMonitoringAround2() throws Throwable {
        /* given */

        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(originMethod);
        when(support.extractObserver(any())).thenReturn(apiObserver);
        when(support.extractSubstituteMethod(any(),any())).thenReturn(substituteMethod);
        when(circuitBreaker.isApiAccessible(apiObserver)).thenReturn(false);

        when(joinPoint.getTarget()).thenReturn(this);
        when(joinPoint.getArgs()).thenReturn(new Object[]{});
        /* when */

        var result = circuitBreakerAspect.apiMonitoringAround(joinPoint);

        /* then */
        assertNotNull(result);
    }

    @Test @DisplayName("API 연결이 정상이므로 본래 메소드가 호출 된다.")
    public void apiMonitoringAround_apiAccessAble() throws Throwable {
        /* given */

        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(originMethod);
        when(support.extractObserver(any())).thenReturn(apiObserver);
        when(support.extractSubstituteMethod(any(),any())).thenReturn(substituteMethod);
        when(circuitBreaker.isApiAccessible(apiObserver)).thenReturn(true);

        when(joinPoint.getTarget()).thenReturn(this);
        when(joinPoint.getArgs()).thenReturn(new Object[]{});

        when(joinPoint.proceed()).thenThrow(OpenApiException.class);
        /* when */

        var result = circuitBreakerAspect.apiMonitoringAround(joinPoint);

        /* then */
        assertNotNull(result);
    }

    @Test
    public void apiMonitoringAround_apiAccessAble_throw() throws Throwable {
        /* given */

        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getTarget()).thenReturn(this);
        when(signature.getMethod()).thenReturn(originMethod);
        when(support.extractObserver(any())).thenReturn(apiObserver);
        when(support.extractSubstituteMethod(any(),any())).thenReturn(substituteMethod);
        when(circuitBreaker.isApiAccessible(apiObserver)).thenReturn(true);

        when(joinPoint.proceed()).thenReturn(new Object());
        /* when */

        var result = circuitBreakerAspect.apiMonitoringAround(joinPoint);

        /* then */
        assertNotNull(result);
    }

}