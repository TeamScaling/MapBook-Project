package com.scaling.libraryservice.commons.circuitBreaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * {@link CircuitBreakerSupporter} 클래스는 {@link CircuitBreaker}에 대한 추가적인 기능을 지원하는 클래스입니다.
 * 이 클래스는 {@link ApiObserver}와 대체 메소드를 추출하는 기능을 제공합니다.
 *
 * {@link ApiObserver}는 API 서버의 상태를 관찰하고, 이 정보를 {@link CircuitBreaker}에 제공하여 불안정한 API 서버를 관리합니다.
 * 대체 메소드는 API 호출이 실패했을 때 호출되는 메소드로, 대체 동작을 제공합니다.
 */
@Component
@Slf4j
public class CircuitBreakerSupporter {

    private Map<Class<? extends ApiObserver>, ApiObserver> observerMap;

    /**
     * 주어진 {@link ApiMonitoring} 인스턴스에 연결된 {@link ApiObserver} 인스턴스를 추출하는 메소드입니다.
     * 이미 이전에 추출된 ApiObserver 인스턴스가 있으면, 그것을 반환합니다.
     * 그렇지 않으면, 새로운 ApiObserver 인스턴스를 생성하고 이를 반환합니다.
     *
     * @param apiMonitoring ApiObserver를 추출할 {@link ApiMonitoring} 인스턴스
     * @return {@link ApiObserver} 인스턴스
     * @throws NoSuchMethodException ApiObserver 클래스에 디폴트 생성자가 없는 경우
     * @throws InvocationTargetException ApiObserver 클래스의 생성자를 호출하는 도중 예외가 발생한 경우
     * @throws InstantiationException ApiObserver 클래스의 인스턴스를 생성하는 도중 예외가 발생한 경우
     * @throws IllegalAccessException ApiObserver 클래스의 생성자에 액세스할 수 없는 경우
     */
    ApiObserver extractObserver(@NonNull ApiMonitoring apiMonitoring)
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

    /**
     * 주어진 {@link ApiMonitoring} 인스턴스와 메소드 배열에서 대체 메소드를 추출하는 메소드입니다.
     * 이 메소드는 ApiMonitoring 인스턴스에서 지정된 대체 메소드 이름을 사용하여 대체 메소드를 찾습니다.
     * 찾을 수 없는 경우 IllegalArgumentException을 발생시킵니다.
     *
     * @param apiMonitoring 대체 메소드 이름을 가져올 {@link ApiMonitoring} 인스턴스
     * @param methods 대체 메소드를 찾을 메소드 배열
     * @return 찾은 대체 메소드
     * @throws IllegalArgumentException 대체 메소드를 찾을 수 없는 경우
     */
    Method extractSubstituteMethod(@NonNull ApiMonitoring apiMonitoring,@NonNull Method[] methods) {

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
