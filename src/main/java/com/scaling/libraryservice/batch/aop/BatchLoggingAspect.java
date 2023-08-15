package com.scaling.libraryservice.batch.aop;

import static com.scaling.libraryservice.logging.logger.TaskType.BATCH_TASK;

import com.scaling.libraryservice.logging.logger.BatchSlackLogger;
import com.scaling.libraryservice.logging.logger.TaskType;
import com.scaling.libraryservice.logging.service.LogService;
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
public class BatchLoggingAspect {
    private final LogService<String> logService;

    @Pointcut("@annotation(com.scaling.libraryservice.batch.aop.BatchLogging)")
    private void enableBatchLogging() {
    }

    @Around("enableBatchLogging()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        String targetNm = joinPoint.getTarget().getClass().getSimpleName();

        logService.slackLogging(BATCH_TASK,targetNm+" start");

        Object result = joinPoint.proceed();

        logService.slackLogging(BATCH_TASK,targetNm+" is completed");

        return result;
    }

}
