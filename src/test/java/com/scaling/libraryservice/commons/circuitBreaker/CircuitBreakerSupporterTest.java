package com.scaling.libraryservice.commons.circuitBreaker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.commons.api.apiConnection.LoanableLibConn;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.assertj.core.internal.bytebuddy.implementation.bytecode.member.MethodInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

@ExtendWith(MockitoExtension.class)
class CircuitBreakerSupporterTest {

    @InjectMocks
    private CircuitBreakerSupporter circuitBreakerSupporter;

    @Mock
    private ProceedingJoinPoint joinPoint;

    private Method originMethod;
    private Method substituteMethod;

    @Mock
    private MethodSignature signature;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        circuitBreakerSupporter = new CircuitBreakerSupporter();
        originMethod = this.getClass().getDeclaredMethod("targetApiMonitoring");
        substituteMethod = this.getClass().getDeclaredMethod("fallBackMethod");
    }

    @ApiMonitoring(apiObserver = LoanableLibConn.class, substitute = "fallBackMethod")
    public void targetApiMonitoring() {

    }

    public void fallBackMethod() {

    }
    @ApiMonitoring(apiObserver = LoanableLibConn.class, substitute = "notFoundMethod")
    public void targetApiMonitoring2(){}

    @Test
    void extractObserver() throws Throwable {
        /* given */
        var method = this.getClass().getDeclaredMethod("targetApiMonitoring");

        var apiMonitoring = method.getAnnotation(ApiMonitoring.class);

        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(originMethod);

        /* when */
        var result = circuitBreakerSupporter.extractObserver(joinPoint);

        /* then */

        assertNotNull(result);
    }

    @Test
    void extractSubstituteMethod() throws NoSuchMethodException {
        /* given */
        var method = this.getClass().getDeclaredMethod("targetApiMonitoring");
        ApiMonitoring apiMonitoring = method.getAnnotation(ApiMonitoring.class);

        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(originMethod);
        when(joinPoint.getTarget()).thenReturn(this);

        /* when */

        var result = circuitBreakerSupporter.extractSubstituteMethod(joinPoint);

        /* then */

        assertEquals(result.getName(), apiMonitoring.substitute());
    }

    @Test
    void extractSubstituteMethod_exception() throws NoSuchMethodException {
        /* given */
        var faultMethod = this.getClass().getDeclaredMethod("targetApiMonitoring2");
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(faultMethod);
        when(joinPoint.getTarget()).thenReturn(this);

        /* when */

        Executable e = () -> circuitBreakerSupporter.extractSubstituteMethod(joinPoint);

        /* then */

        assertThrows(IllegalArgumentException.class,e);
    }


}