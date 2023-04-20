package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.entity.LibraryMeta;
import lombok.Getter;

@Getter
public class LibraryMetaDto {

    private final Integer id;
    private final Integer areaCd;
    private final Long count;
    private final String oneArea;
    private final String twoArea;

    public LibraryMetaDto(LibraryMeta libraryMeta) {
        this.id = libraryMeta.getId();
        this.areaCd = libraryMeta.getAreaCd();
        this.count = libraryMeta.getCount();
        this.oneArea = libraryMeta.getOneArea();
        this.twoArea = libraryMeta.getTwoArea();
    }
}
