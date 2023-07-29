package com.scaling.libraryservice.mapBook.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

/**
 * 대출 가능한 도서관 응답 데이터.
 */
@Getter @Setter
@ToString
public class RespMapBookDto {

    private String isbn13;
    private String libCode;
    private String libNm;
    private boolean hasBook;
    private Double libLo;
    private Double libLa;
    private String libArea;
    private String libUrl;
    private Integer areaCd;
    private boolean available;

    public RespMapBookDto(@NonNull ApiLoanableLibDto dto, @NonNull LibraryInfoDto libraryInfoDto) {

        this.isbn13 = dto.getIsbn13();
        this.libCode = dto.getLibCode();
        this.hasBook = dto.isLoanAble();
        this.libLo = libraryInfoDto.getLibLon();
        this.libLa = libraryInfoDto.getLibLat();
        this.libArea = libraryInfoDto.getLibArea();
        this.libNm = libraryInfoDto.getLibNm();
        this.libUrl = libraryInfoDto.getLibUrl();
        this.areaCd = libraryInfoDto.getAreaCd();
        this.available = String.valueOf(dto.getLoanAvailable()).equals("Y");
    }

    public static RespMapBookDto responseWithLoanable(ApiLoanableLibDto dto, @NonNull LibraryInfoDto libraryInfoDto){
        return new RespMapBookDto(dto, libraryInfoDto);
    }


    public RespMapBookDto(@NonNull ReqMapBookDto reqMapBookDto,@NonNull LibraryInfoDto libraryInfoDto,boolean loanAvailable) {

        this.isbn13 = reqMapBookDto.getIsbn();
        this.libCode = String.valueOf(libraryInfoDto.getLibNo());
        this.hasBook = libraryInfoDto.isHasBook();
        this.libLo = libraryInfoDto.getLibLon();
        this.libLa = libraryInfoDto.getLibLat();
        this.libArea = libraryInfoDto.getLibArea();
        this.libNm = libraryInfoDto.getLibNm();
        this.libUrl = libraryInfoDto.getLibUrl();
        this.areaCd = libraryInfoDto.getAreaCd();

        this.available = loanAvailable;
    }




}
