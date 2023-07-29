package com.scaling.libraryservice.commons.circuitBreaker;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.commons.api.apiConnection.LoanableLibConn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class CircuitBreakerSupporterTest {

    private final CircuitBreakerSupporter circuitBreakerSupporter = new CircuitBreakerSupporter();

    @ApiMonitoring(api = LoanableLibConn.class, substitute = "fallBackMethod")
    public void targetApiMonitoring() {

    }

    public void fallBackMethod() {

    }
    @ApiMonitoring(api = LoanableLibConn.class, substitute = "notFoundMethod")
    public void targetApiMonitoring2(){}

    @Test
    void extractObserver() throws Throwable {
        /* given */
        var method = this.getClass().getDeclaredMethod("targetApiMonitoring");

        var apiMonitoring = method.getAnnotation(ApiMonitoring.class);

        /* when */

        var result = circuitBreakerSupporter.extractObserver(apiMonitoring);

        /* then */

        assertNotNull(result);
    }

    @Test
    void extractSubstituteMethod() throws NoSuchMethodException {
        /* given */
        var method = this.getClass().getDeclaredMethod("targetApiMonitoring");
        ApiMonitoring apiMonitoring = method.getAnnotation(ApiMonitoring.class);

        /* when */

        var result = circuitBreakerSupporter.extractSubstituteMethod(apiMonitoring,
            this.getClass().getMethods());

        /* then */

        assertEquals(result.getName(), apiMonitoring.substitute());
    }

    @Test
    void extractSubstituteMethod_exception() throws NoSuchMethodException {
        /* given */
        var method = this.getClass().getDeclaredMethod("targetApiMonitoring2");
        ApiMonitoring apiMonitoring = method.getAnnotation(ApiMonitoring.class);

        /* when */

        Executable e = () -> circuitBreakerSupporter.extractSubstituteMethod(apiMonitoring,
            this.getClass().getMethods());

        /* then */

        assertThrows(IllegalArgumentException.class,e);
    }


}