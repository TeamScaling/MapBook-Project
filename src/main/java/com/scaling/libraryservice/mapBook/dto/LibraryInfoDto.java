package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.entity.LibraryInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

/**
 *  도서관 정보 LibraryInfo Entity에 대한 DTO
 */
@Getter
@ToString
@Slf4j @Builder
@AllArgsConstructor
public class LibraryInfoDto {

    private final String libNm;

    private final Integer libNo;

    private final Double libLon;

    private final Double libLat;

    private final String libArea;

    private final String libUrl;
    private final Integer areaCd;

    private boolean hasBook = false;

    private boolean isHasBookSupport = false;


    public LibraryInfoDto(@NonNull LibraryInfo libraryInfo) {
        this.libNm = libraryInfo.getLibNm();
        this.libNo = libraryInfo.getLibNo();
        this.libLon = libraryInfo.getLibLon();
        this.libLat = libraryInfo.getLibLat();
        this.libArea = libraryInfo.getLibArea();
        this.libUrl = libraryInfo.getLibUrl();
        this.areaCd = libraryInfo.getAreaCd();
    }


    public LibraryInfoDto(LibraryInfo libraryInfo,boolean hasBook,boolean isHasBookSupport) {
        this.libNm = libraryInfo.getLibNm();
        this.libNo = libraryInfo.getLibNo();
        this.libLon = libraryInfo.getLibLon();
        this.libLat = libraryInfo.getLibLat();
        this.libArea = libraryInfo.getLibArea();
        this.libUrl = libraryInfo.getLibUrl();
        this.areaCd = libraryInfo.getAreaCd();
        this.hasBook = hasBook;
        this.isHasBookSupport = isHasBookSupport;
    }

    public static LibraryInfoDto hasBookLibDto(LibraryInfo libraryInfo){
        return new LibraryInfoDto(libraryInfo,true,true);
    }


}
