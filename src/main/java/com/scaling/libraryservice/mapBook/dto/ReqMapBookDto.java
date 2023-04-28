package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.caching.CacheKey;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter @Setter @ToString
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

    public ReqMapBookDto(String isbn, Double lat, Double lon, String oneArea, String twoArea) {
        this.isbn = isbn;
        this.lat = lat;
        this.lon = lon;
        this.oneArea = oneArea;
        this.twoArea = twoArea;
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

    public void updateAreaCd(){

        this.areaCd = LibraryFindService.outPutAreaCd(this);
    }
}
