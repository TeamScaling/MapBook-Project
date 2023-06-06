package com.scaling.libraryservice.mapBook.cacheKey;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.ToString;

/**
 * {@link HasBookCacheKey} 클래스는 도서 정보를 캐싱하기 위한 캐시 키를 구현합니다.
 * 이 클래스는 ISBN-13과 지역 코드를 사용하여 도서 정보를 식별하며, 캐시에서 해당 도서 정보를 조회하거나 저장할 때 사용됩니다.
 */
@ToString
public class HasBookCacheKey implements CacheKey<HasBookCacheKey, List<LibraryDto>> {

    private final String isbn13;

    private final Integer areaCd;

    public HasBookCacheKey(String isbn13, Integer areaCd) {
        this.isbn13 = isbn13;
        this.areaCd = areaCd;
    }

    @Override
    public Cache<HasBookCacheKey, List<LibraryDto>> configureCache() {
        return Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS)
            .maximumSize(1000)
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HasBookCacheKey that = (HasBookCacheKey) o;
        return Objects.equals(isbn13, that.isbn13) && Objects.equals(areaCd,
            that.areaCd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn13, areaCd);
    }
}
