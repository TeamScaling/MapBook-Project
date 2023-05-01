package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.entity.Library;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

// Library entity를 담는 dto
@Getter
@Setter
@ToString
@Slf4j
public class LibraryDto {

    private String libNm;

    private Integer libNo;

    private Double libLon;

    private Double libLat;

    private String libArea;

    private String libUrl;

    private String oneAreaNm;

    private String twoAreaNm;
    private Integer areaCd;

    private String hasBook;

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

    public LibraryDto(Integer libNo) {
        this.libNo = libNo;
    }

    public String getFullAreaNm() {

        return this.oneAreaNm + " " + this.twoAreaNm;
    }
}
