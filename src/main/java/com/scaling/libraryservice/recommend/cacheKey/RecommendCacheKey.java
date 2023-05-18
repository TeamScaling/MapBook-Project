package com.scaling.libraryservice.recommend.cacheKey;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.caching.CacheKey;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.ToString;


/**
 * 추천 도서 요청에 대한 결과를 캐싱하기 위한 Key
 */
@ToString
public class RecommendCacheKey implements CacheKey<RecommendCacheKey, List<String>> {

    private final String query;

    public RecommendCacheKey(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public Cache<RecommendCacheKey, List<String>> configureCache() {
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
        RecommendCacheKey that = (RecommendCacheKey) o;
        return Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query);
    }
}
