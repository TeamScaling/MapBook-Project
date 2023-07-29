package com.scaling.libraryservice.commons.timer;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TimerAop {

    private static final String TIME_BOUND = "%.3f";

    @Pointcut("@annotation(com.scaling.libraryservice.commons.timer.MeasureTaskTime)")
    private void enableTimer() {
    }

    @Around("enableTimer()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable { //메서드 실행시 걸린시간 측정

        return executeAndMeasureTime(joinPoint);
    }

    private Object executeAndMeasureTime(ProceedingJoinPoint joinPoint)
        throws Throwable {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed(); //메서드가 실행되는 부분
        stopWatch.stop();
        // double 범위 초과로 인한 지수 표현 방지
        String totalTime = String.format(TIME_BOUND, stopWatch.getTotalTimeSeconds());

        if (isTimeMeasurable(result)) {
            addSearchTimeInResponse(result, totalTime);
        }

        return result;
    }

    private void addSearchTimeInResponse(Object result, String totalTime) {
        ((TimeMeasurable<?>) result).addMeasuredTime(totalTime);
    }

    private boolean isTimeMeasurable(Object result) {
        return result instanceof TimeMeasurable<?>;
    }

}