package com.scaling.libraryservice.commons.circuitBreaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CircuitBreakerSupporter {

    private Map<Class<? extends ApiObserver>, ApiObserver> observerMap;

    public ApiObserver extractObserver(@NonNull ApiMonitoring apiMonitoring)
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        if (observerMap == null){
            observerMap = new HashMap<>();
        }

        Class<? extends ApiObserver> observerClazz = apiMonitoring.api();

        if(observerMap.containsKey(observerClazz)){

            return observerMap.get(observerClazz);
        }else{
            Constructor<? extends ApiObserver> constructor = apiMonitoring.api()
                .getDeclaredConstructor();

            constructor.setAccessible(true);

            ApiObserver apiObserver = constructor.newInstance();

            observerMap.put(observerClazz, apiObserver);

            return apiObserver;
        }
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
