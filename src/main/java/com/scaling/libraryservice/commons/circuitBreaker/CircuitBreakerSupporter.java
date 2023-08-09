package com.scaling.libraryservice.commons.circuitBreaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * {@link CircuitBreakerSupporter} 클래스는 {@link CircuitBreaker}에 대한 추가적인 기능을 지원하는 클래스입니다. 이 클래스는
 * {@link ApiObserver}와 대체 메소드를 추출하는 기능을 제공합니다.
 * <p>
 * {@link ApiObserver}는 API 서버의 상태를 관찰하고, 이 정보를 {@link CircuitBreaker}에 제공하여 불안정한 API 서버를 관리합니다. 대체
 * 메소드는 API 호출이 실패했을 때 호출되는 메소드로, 대체 동작을 제공합니다.
 */
@Component
@Slf4j
public class CircuitBreakerSupporter {

    private final Map<Class<? extends ApiObserver>, ApiObserver> observerMap;

    public CircuitBreakerSupporter() {
        this.observerMap = new HashMap<>();
    }

    /**
     * 주어진 {@link ApiMonitoring} 인스턴스에 연결된 {@link ApiObserver} 인스턴스를 추출하는 메소드입니다. 이미 이전에 추출된
     * ApiObserver 인스턴스가 있으면, 그것을 반환합니다. 그렇지 않으면, 새로운 ApiObserver 인스턴스를 생성하고 이를 반환합니다.
     *
     * @return {@link ApiObserver} 인스턴스
     */
    ApiObserver extractObserver(@NonNull ProceedingJoinPoint joinPoint) throws Exception {

        ApiMonitoring apiMonitoring = getApiMonitoring(joinPoint);
        Class<? extends ApiObserver> observerClazz = apiMonitoring.apiObserver();

        return observerMap.containsKey(observerClazz) ?
            observerMap.get(observerClazz) :
            constructAndPutObserver(apiMonitoring, observerClazz);
    }

    ApiObserver constructAndPutObserver(
        ApiMonitoring apiMonitoring, Class<? extends ApiObserver> observerClazz
    ) throws Exception {

        Constructor<? extends ApiObserver> constructor =
            apiMonitoring
                .apiObserver()
                .getDeclaredConstructor();

        constructor.setAccessible(true);

        ApiObserver apiObserver = constructor.newInstance();
        observerMap.put(observerClazz, apiObserver);

        return apiObserver;
    }


    /**
     * 주어진 {@link ApiMonitoring} 인스턴스와 메소드 배열에서 대체 메소드를 추출하는 메소드입니다. 이 메소드는 ApiMonitoring 인스턴스에서 지정된
     * 대체 메소드 이름을 사용하여 대체 메소드를 찾습니다. 찾을 수 없는 경우 IllegalArgumentException을 발생시킵니다.
     *
     * @return 찾은 대체 메소드
     */
    Method extractSubstituteMethod(ProceedingJoinPoint joinPoint) throws IllegalArgumentException {

        Method[] methods = getMethods(joinPoint);
        ApiMonitoring apiMonitoring = getApiMonitoring(joinPoint);

        return getSubstituteMethod(apiMonitoring, methods);
    }

    Method getSubstituteMethod(ApiMonitoring apiMonitoring, Method[] methods) {

        return Arrays.stream(methods)
            .filter(method -> isSubstituteMethod(method, apiMonitoring.substitute()))
            .findAny()
            .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isSubstituteMethod(Method method, String substituteNm) {
        return method.getName().equals(substituteNm);
    }

    private Method[] getMethods(ProceedingJoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getDeclaredMethods();
    }

    private ApiMonitoring getApiMonitoring(ProceedingJoinPoint joinPoint) {

        Method joinPointMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        return joinPointMethod.getAnnotation(ApiMonitoring.class);
    }

}
