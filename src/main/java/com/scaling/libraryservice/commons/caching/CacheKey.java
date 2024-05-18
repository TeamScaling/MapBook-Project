package com.scaling.libraryservice.commons.caching;

import com.github.benmanes.caffeine.cache.Cache;

/**
 * 커스텀 캐시 관리자({@link MapBookCacheManager})에서 관리할 캐싱 데이터를 식별하기 위한 각각의 캐시 키 인터페이스입니다.
 */
public interface CacheKey<K,I> {

    Cache<? extends CacheKey<K,I>,I> configureCache();

}
