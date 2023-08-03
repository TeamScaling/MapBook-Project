package com.scaling.libraryservice.commons.circuitBreaker;

import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Api 장애 발생 시, 기존의 Api를 이용한 서비스 대신 대체 가능한 서비스로 대체하기 위한 사용자 정의 AOP 클래스 입니다.
 */

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CircuitBreakerAspect {

    private final CircuitBreaker circuitBreaker;

    private final CircuitBreakerSupporter support;

    /**
     * Annotation 방식으로 @ApiMonitoring가 붙은 대상을 target으로 선정 합니다.
     */
    @Pointcut("@annotation(com.scaling.libraryservice.commons.circuitBreaker.ApiMonitoring)")
    public void apiMonitoringPointcut() {
    }

    /**
     * 이 메소드는 대상 서비스에 대한 API 연결을 모니터링합니다. API 연결에 문제가 있으면 다른 서비스로 전환하여 문제를 우회합니다.
     *
     * @param joinPoint 이는 정상적으로 실행되던 기존 객체의 메소드를 의미합니다.
     * @return API 연결이 정상적인 경우 기존 서비스의 결과 값을 반환하고, 연결에 장애가 있는 경우 대체 서비스의 결과 값을 반환합니다.
     * @throws Throwable 모든 예외를 던질 수 있는 상위 예외입니다. 이 메소드는 실행 도중 다양한 예외가 발생할 수 있으므로, 이를 호출하는 측에서는 이
     *                   메소드가 던지는 예외를 적절히 처리해야 합니다.
     */
    @Around("apiMonitoringPointcut()")
    public Object apiMonitoringAround(@NonNull ProceedingJoinPoint joinPoint)
        throws Throwable {

        Method joinPointMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        ApiMonitoring apiMonitoring = joinPointMethod.getAnnotation(ApiMonitoring.class);


        Method[] methods = joinPoint.getTarget().getClass().getDeclaredMethods();

        ApiObserver apiObserver = support.extractObserver(apiMonitoring);
        Method fallBackMethod = support.extractSubstituteMethod(apiMonitoring, methods);

        if (!circuitBreaker.isApiAccessible(apiObserver)) {

            return fallBackMethod.invoke(joinPoint.getTarget(), joinPoint.getArgs());
        } else {

            try {
                return joinPoint.proceed();

            } catch (OpenApiException e) {
                circuitBreaker.receiveError(apiObserver);
                fallBackMethod.setAccessible(true);

                return fallBackMethod.invoke(joinPoint.getTarget(), joinPoint.getArgs());
            }
        }
    }


}
