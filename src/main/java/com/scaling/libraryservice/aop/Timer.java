package com.scaling.libraryservice.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @Retention(RetentionPolicy.RUNTIME: 해당 어노테이션을 런타임까지 살아있도록 설정)
// @Target(ElementType.TYPE, METHOD)}: 클래스, 인터페이스, 열거 타입, 메소드 필드에 어노테이션 사용 가능
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Timer {}
