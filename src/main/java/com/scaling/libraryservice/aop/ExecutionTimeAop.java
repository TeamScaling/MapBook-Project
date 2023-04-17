package com.scaling.libraryservice.aop;


import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Log4j2
public class ExecutionTimeAop {

	@Around("execution(* *..service.*.*(..))")
	public Object calculateExecutionTime(ProceedingJoinPoint pjp) throws Throwable
	{
		StopWatch sw = new StopWatch();
		sw.start();

		Object result = pjp.proceed();

		sw.stop();
		long executionTime = sw.getTotalTimeMillis();

		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName();

		String task = className + " & " + methodName;

		log.info("[ExecutionTime] " + task + "-->" + executionTime + "(ms)");

		return result;

	}

}
