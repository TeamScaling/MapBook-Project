package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.commons.caching.CacheKey;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.lang.Nullable;

/**
 * 사용자 주변 도서관 중 대출 가능 도서관을 찾기 위한 Http 요청 DTO이다.
 * {@link CacheKey}를 구현하여, {@link CustomCacheManager}
 * 에서 캐싱 된 데이터를 찾기 위한 Key로 사용 될 수 있다.
 */
@Getter @Setter
public class ReqMapBookDto implements CacheKey {

    private String isbn;
    private Double lat;
    private Double lon;
    private String oneArea;
    private String twoArea;
    @Nullable
    private Integer areaCd;

    public boolean isAddressRequest(){

        return oneArea != null & twoArea != null;
    }

    public ReqMapBookDto() {
    }

    @Override
    public String toString() {
        return "ReqMapBookDto(" +
            "isbn=" + isbn+
            ", areaCd=" + areaCd +
            ')';
    }

    public ReqMapBookDto(String isbn, Double lat, Double lon, String oneArea, String twoArea) {
        this.isbn = isbn;
        this.lat = lat;
        this.lon = lon;
        this.oneArea = oneArea;
        this.twoArea = twoArea;
    }



    public ReqMapBookDto(JSONObject jsonObject){
        this.isbn = jsonObject.getString("isbn");
        this.lat = Double.valueOf(jsonObject.getString("lat"));
        this.lon = Double.valueOf(jsonObject.getString("lon"));
        this.oneArea = jsonObject.getString("oneArea");
        this.twoArea = jsonObject.getString("twoArea");

    }

    public ReqMapBookDto(String isbn, @Nullable Integer areaCd) {
        this.isbn = isbn;
        this.areaCd = areaCd;
    }

    public boolean isValidCoordinate(){

        return this.lat > 33.173360 & this.lat < 38.319297
            & this.lon > 126.559157 & this.lon <127.225938;
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
        return Objects.equals(isbn, that.isbn) && Objects.equals(areaCd,
            that.areaCd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, areaCd);
    }

    /**
     *  사용자 요청에서 위/경도 데이터를 지역코드로 변환 한다.
     */
    public void updateAreaCd(){

        this.areaCd = LibraryFindService.outPutAreaCd(this);
    }
}
