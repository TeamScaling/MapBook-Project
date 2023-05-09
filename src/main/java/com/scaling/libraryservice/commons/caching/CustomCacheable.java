package com.scaling.libraryservice.commons.caching;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 커스텀 캐시 어노테이션으로, 메서드의 결과를 캐싱할 때 사용합니다.
 * 이 어노테이션을 사용하려면 {@link CustomCacheManager}와 함께 사용되어야 합니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomCacheable {

    String cacheName();
}