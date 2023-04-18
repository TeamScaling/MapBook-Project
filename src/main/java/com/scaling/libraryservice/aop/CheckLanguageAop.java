package com.scaling.libraryservice.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class CheckLanguageAop {

	@Pointcut("@annotation(CheckLanguage)")
    private void enableCheck() {
    }

	@Around("enableCheck()")
	public Object checkLanguage(ProceedingJoinPoint pjp) throws Throwable
	{

		Object result = pjp.proceed();

		Object[] args = pjp.getArgs();

		String query = (String) args[0];
		if (isEnglish(query)) {
			log.info("[CheckLanguage] " + query + "은(는) 영어입니다.");
		} else {
			log.info("[CheckLanguage] " + query  +  "(은)는 한글입니다.");
		}

		return result;
	}

	public static boolean isEnglish(String input){
		String pattern = "^[a-zA-Z\\s+]+$";
		return input.matches(pattern);
	}

}
