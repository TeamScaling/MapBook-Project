package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.entity.Library;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

/**
 *  도서관 정보 Library Entity에 대한 DTO
 */
@Getter
@ToString
@Slf4j @Builder
@AllArgsConstructor
public class LibraryDto {

    private final String libNm;

    private final Integer libNo;

    private final Double libLon;

    private final Double libLat;

    private final String libArea;

    private final String libUrl;
    private final Integer areaCd;

    private String hasBook = "N";

    private boolean isHasBookSupport = false;


    public LibraryDto(@NonNull Library library) {
        this.libNm = library.getLibNm();
        this.libNo = library.getLibNo();
        this.libLon = library.getLibLon();
        this.libLat = library.getLibLat();
        this.libArea = library.getLibArea();
        this.libUrl = library.getLibUrl();
        this.areaCd = library.getAreaCd();
    }

    public LibraryDto(@NonNull Library library,String hasBook) {
        this.libNm = library.getLibNm();
        this.libNo = library.getLibNo();
        this.libLon = library.getLibLon();
        this.libLat = library.getLibLat();
        this.libArea = library.getLibArea();
        this.libUrl = library.getLibUrl();
        this.areaCd = library.getAreaCd();
        this.hasBook = hasBook;
    }

    public LibraryDto(Library library,String hasBook,boolean isHasBookSupport) {
        this.libNm = library.getLibNm();
        this.libNo = library.getLibNo();
        this.libLon = library.getLibLon();
        this.libLat = library.getLibLat();
        this.libArea = library.getLibArea();
        this.libUrl = library.getLibUrl();
        this.areaCd = library.getAreaCd();
        this.hasBook = hasBook;
        this.isHasBookSupport = isHasBookSupport;
    }

    public boolean hasBook(){
        return hasBook.equals("Y");
    }

}
