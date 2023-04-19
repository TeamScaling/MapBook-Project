package com.scaling.libraryservice.mapBook.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter @Setter @ToString
public class ReqMapBookDto {

    private final String isbn;
    private final double lat;

    private final double lon;
    private final String oneArea;
    private final String twoArea;

    public boolean isAddressRequest(){

        return oneArea != null & twoArea != null;
    }

    public boolean isValidCoordinate(){

        return this.lat > 33.173360 & this.lat < 38.319297
            & this.lon > 126.559157 & this.lon <127.225938;
    }

}
