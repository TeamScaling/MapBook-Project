package com.scaling.libraryservice.commons.circuitBreaker;

import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.stereotype.Component;

@Component
public class CircuitBreakerSupport {

    public ApiObserver extractObserver(ApiMonitoring apiMonitoring) throws Throwable {

        Constructor<? extends ApiObserver> constructor = apiMonitoring.api()
            .getDeclaredConstructor();

        constructor.setAccessible(true);

        return constructor.newInstance();
    }

    public Method extractSubstituteMethod(ApiMonitoring apiMonitoring, Method[] methods) {

        String substituteNm = apiMonitoring.substitute();

        Method substitute = null;

        for (Method m : methods) {
            if (m.getName().contains(substituteNm)) {
                substitute = m;
            }
        }

        if (substitute == null) {
            throw new IllegalArgumentException();
        }

        return substitute;

    }

}
