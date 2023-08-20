package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.entity.LibraryMeta;
import lombok.Getter;
import org.springframework.lang.NonNull;

/**
 *  전국 도서관 통계 LibraryMeta Entity에 대한 DTO
 */
@Getter
public class LibraryMetaDto {
    private final Integer areaCd;
    private final String oneArea;

    public LibraryMetaDto(@NonNull LibraryMeta libraryMeta) {
        this.areaCd = libraryMeta.getAreaCd();
        this.oneArea = libraryMeta.getOneArea();
    }
}
