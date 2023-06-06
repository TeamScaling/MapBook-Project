package com.scaling.libraryservice.mapBook.dto;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

/**
 * 사용자 주변 도서관 중 대출 가능 도서관을 찾기 위한 Http 요청 DTO이다. {@link CacheKey}를 구현하여, {@link CustomCacheManager}
 * 에서 캐싱 된 데이터를 찾기 위한 Key로 사용 될 수 있다.
 */
@Getter
@Builder
@AllArgsConstructor
public class ReqMapBookDto implements CacheKey<ReqMapBookDto, List<RespMapBookDto>> {

    private String isbn;
    private Double lat;
    private Double lon;

    private int areaCd;

    public ReqMapBookDto() {
    }

    @Override
    public Cache<ReqMapBookDto, List<RespMapBookDto>> configureCache() {
        return Caffeine.newBuilder()
            .expireAfterWrite(6, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();
    }


    public ReqMapBookDto(String isbn, Double lat, Double lon) {
        this.isbn = isbn;
        this.lat = lat;
        this.lon = lon;
    }


    public ReqMapBookDto(@NonNull JSONObject jsonObject) {
        this.isbn = jsonObject.getString("isbn");
        this.lat = Double.valueOf(jsonObject.getString("lat"));
        this.lon = Double.valueOf(jsonObject.getString("lon"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReqMapBookDto that = (ReqMapBookDto) o;
        return Objects.equals(isbn, that.isbn) && Objects.equals(areaCd, that.areaCd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, areaCd);
    }

    public void setAreaCd(int areaCd) {
        this.areaCd = areaCd;
    }
}
