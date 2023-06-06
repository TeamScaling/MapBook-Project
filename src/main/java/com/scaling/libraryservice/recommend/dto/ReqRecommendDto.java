package com.scaling.libraryservice.recommend.dto;

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
public class ReqRecommendDto implements CacheKey<ReqRecommendDto, List<String>> {

    private String query;

    public ReqRecommendDto() {
    }

    public ReqRecommendDto(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public Cache<ReqRecommendDto, List<String>> configureCache() {
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
        ReqRecommendDto that = (ReqRecommendDto) o;
        return Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query);
    }
}
