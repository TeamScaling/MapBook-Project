package com.scaling.libraryservice.dataPipe.aop;

import com.scaling.libraryservice.logging.logger.BatchLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class BatchLoggingAop {

    private final BatchLogger batchLogger;

    @Pointcut("@annotation(com.scaling.libraryservice.dataPipe.aop.BatchLogging)")
    private void enableBatchLogging() {
    }

    @Around("enableBatchLogging()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        String targetNm = joinPoint.getTarget().getClass().getSimpleName();

        batchLogger.sendLogToSlack(targetNm+" is start");

        Object result = joinPoint.proceed();

        batchLogger.sendLogToSlack(targetNm+" is completed");

        return result;
    }

}
