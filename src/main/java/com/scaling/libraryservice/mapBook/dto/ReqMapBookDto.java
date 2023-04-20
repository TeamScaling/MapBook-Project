package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@RequiredArgsConstructor
@Getter @Setter @ToString
public class ReqMapBookDto {

    private final String isbn;
    private final Double lat;
    private final Double lon;
    private final String oneArea;
    private final String twoArea;
    @Nullable
    private Integer areaCd;

    public boolean isAddressRequest(){

        return oneArea != null & twoArea != null;
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
