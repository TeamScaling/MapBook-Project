package com.scaling.libraryservice.search.cacheKey;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.ToString;

/**
 * 검색된 도서 데이터를 캐싱하기 위한 키 클래스입니다.
 * 이 클래스는 캐싱 시 사용되는 고유한 키 값을 생성하고 관리합니다.
 */
@ToString
public class BookCacheKey implements CacheKey<BookCacheKey,RespBooksDto>{

    private final String query;
    private final int page;

    public String getQuery() {
        return query;
    }

    public int getPage() {
        return page;
    }

    public BookCacheKey(String query, int page) {
        this.query = query;
        this.page = page;
    }

    @Override
    public Cache<BookCacheKey, RespBooksDto> configureCache() {
        return Caffeine.newBuilder().expireAfterWrite(1,TimeUnit.DAYS).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookCacheKey that = (BookCacheKey) o;
        return page == that.page && Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, page);
    }
}
