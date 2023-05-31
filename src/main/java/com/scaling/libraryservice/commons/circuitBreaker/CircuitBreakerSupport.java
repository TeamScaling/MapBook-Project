package com.scaling.libraryservice.commons.circuitBreaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CircuitBreakerSupport {

    public ApiObserver extractObserver(@NonNull ApiMonitoring apiMonitoring) throws Throwable {

        Constructor<? extends ApiObserver> constructor = apiMonitoring.api()
            .getDeclaredConstructor();

        constructor.setAccessible(true);

        return constructor.newInstance();
    }

    public Method extractSubstituteMethod(@NonNull ApiMonitoring apiMonitoring,@NonNull Method[] methods) {

        String substituteNm = apiMonitoring.substitute();

        Method substitute = null;

        for (Method m : methods) {
            if (m.getName().equals(substituteNm)) {
                substitute = m;
            }
        }

        if (substitute == null) {
            throw new IllegalArgumentException();
        }

        return substitute;

    }

}
