package com.scaling.libraryservice.commons.circuitBreaker;

import com.scaling.libraryservice.mapBook.controller.MapBookController;
import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CircuitBreakerAspect<T> {

    @Pointcut("@annotation(com.scaling.libraryservice.commons.circuitBreaker.Substitutable)")
    public void substitutablePointcut() {}

    @Around("substitutablePointcut()")
    public Object substitutableAround(ProceedingJoinPoint joinPoint) throws Throwable {

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Substitutable annotation = method.getAnnotation(Substitutable.class);

        Class<?> oClazz = annotation.origin();
        String substituteNm = annotation.substitute();

        Method substitute = null;

        for (Method m : MapBookController.class.getMethods()){

            if(m.getName().contains(substituteNm)){
                substitute = m;
            }
        }

        ApiObserver apiObserver = (ApiObserver) oClazz.getConstructor().newInstance();

        if (!apiObserver.getApiStatus().apiAccessible() & substitute != null) {
            return substitute.invoke(joinPoint.getTarget(), joinPoint.getArgs());
        }

        return joinPoint.proceed();
    }

}
