package com.scaling.libraryservice.commons.timer;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class TimerAop {

    @Pointcut("@annotation(com.scaling.libraryservice.commons.timer.Timer)")//Timer 어노테이션이 붙은 메서드에만 적용
    private void enableTimer() {
    }

    @Around("enableTimer()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable { //메서드 실행시 걸린시간 측정
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed(); //메서드가 실행되는 부분

        stopWatch.stop();

        String joinPointNm = joinPoint.getTarget().getClass().getName();

        // double 범위 초과로 인한 지수 표현 방지
        String totalTime = String.format("%.6f",stopWatch.getTotalTimeSeconds());


        log.info("[{}s] in [{}]",totalTime,joinPointNm);

        return result;
    }
}