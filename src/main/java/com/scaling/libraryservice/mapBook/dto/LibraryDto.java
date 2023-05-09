package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.entity.Library;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 *  도서관 정보 Library Entity에 대한 DTO
 */
@Getter
@ToString
@Slf4j
public class LibraryDto {

    private final String libNm;

    private final Integer libNo;

    private final Double libLon;

    private final Double libLat;

    private final String libArea;

    private final String libUrl;

    private final String oneAreaNm;

    private final String twoAreaNm;
    private final Integer areaCd;

    private final String hasBook;

    private boolean isSupportedArea;

    public LibraryDto(Library library) {
        this.libNm = library.getLibNm();
        this.libNo = library.getLibNo();
        this.libLon = library.getLibLon();
        this.libLat = library.getLibLat();
        this.libArea = library.getLibArea();
        this.libUrl = library.getLibUrl();
        this.oneAreaNm = library.getOneAreaNm();
        this.twoAreaNm = library.getTwoAreaNm();
        this.areaCd = library.getAreaCd();
        this.hasBook = "N";
    }

    public LibraryDto(Library library,String hasBook) {
        this.libNm = library.getLibNm();
        this.libNo = library.getLibNo();
        this.libLon = library.getLibLon();
        this.libLat = library.getLibLat();
        this.libArea = library.getLibArea();
        this.libUrl = library.getLibUrl();
        this.oneAreaNm = library.getOneAreaNm();
        this.twoAreaNm = library.getTwoAreaNm();
        this.areaCd = library.getAreaCd();
        this.hasBook = hasBook;
    }

    public LibraryDto(Library library,String hasBook,boolean isSupportedArea) {
        this.libNm = library.getLibNm();
        this.libNo = library.getLibNo();
        this.libLon = library.getLibLon();
        this.libLat = library.getLibLat();
        this.libArea = library.getLibArea();
        this.libUrl = library.getLibUrl();
        this.oneAreaNm = library.getOneAreaNm();
        this.twoAreaNm = library.getTwoAreaNm();
        this.areaCd = library.getAreaCd();
        this.hasBook = hasBook;
        this.isSupportedArea = isSupportedArea;
    }

    public String getFullAreaNm() {

        return this.oneAreaNm + " " + this.twoAreaNm;
    }
}
