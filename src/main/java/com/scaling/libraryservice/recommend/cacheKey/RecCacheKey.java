package com.scaling.libraryservice.recommend.cacheKey;

import com.scaling.libraryservice.commons.caching.CacheKey;
import java.util.Objects;

/**
 * 추천 도서 요청에 대한 결과를 캐싱하기 위한 Key
 */
public class RecCacheKey implements CacheKey {

    private final String query;

    public RecCacheKey(String query) {
        this.query = query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecCacheKey that = (RecCacheKey) o;
        return Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query);
    }
}
