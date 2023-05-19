package com.scaling.libraryservice.commons.circuitBreaker;

import com.scaling.libraryservice.mapBook.controller.MapBookController;
import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * Api 장애 발생 시, 기존의 Api를 이용한 서비스 대신 대체 가능한 서비스로 대체하기 위한 사용자 정의 AOP 클래스 입니다.
 */

@Aspect
@Component
@Slf4j @RequiredArgsConstructor
public class CircuitBreakerAspect {

    private final CircuitBreaker circuitBreaker;

    /**
     * Annotation 방식으로 @Substitutable가 붙은 대상을 target으로 선정 합니다.
     */
    @Pointcut("@annotation(com.scaling.libraryservice.commons.circuitBreaker.ApiMonitoring)")
    public void apiMonitoringPointcut() {}

    /**
     * 타겟이 되는 대상에 대해 API 연결 상태를 확인 후, 장애 땐 다른 서비스로 우회 합니다.
     * @param joinPoint 기존에 정상적으로 실행되던 기존 객체의 메소드
     * @return API 연결 정상일 경우엔 기존의 서비스 결과 값 반환, 연결 장애일 경우엔 대체 서비스 결과 값 반환
     * @throws Throwable
     *
     */
    @Around("apiMonitoringPointcut()")
    public Object apiMonitoringAround(ProceedingJoinPoint joinPoint)
        throws Throwable {

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        ApiMonitoring annotation = method.getAnnotation(ApiMonitoring.class);

        Class<? extends ApiObserver> observerClass = annotation.api();
        String substituteNm = annotation.substitute();

        ApiObserver apiObserver = observerClass.getConstructor().newInstance();

        Method substitute = null;

        for (Method m : joinPoint.getClass().getDeclaredMethods()){
            if(m.getName().contains(substituteNm)){
                substitute = m;
            }
        }

        if(substitute == null){
            throw new IllegalArgumentException();
        }

        if (!circuitBreaker.isClosed(apiObserver)) {
            return substitute.invoke(joinPoint.getTarget(), joinPoint.getArgs());
        }

        Object result = null;

        try{
            result = joinPoint.proceed();
        }catch (OpenApiException e){
            circuitBreaker.receiveError(apiObserver);
            return substitute.invoke(joinPoint.getTarget(), joinPoint.getArgs());
        }
        return result;
    }


}
