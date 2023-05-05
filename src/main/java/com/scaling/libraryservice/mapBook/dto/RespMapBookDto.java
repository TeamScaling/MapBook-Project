package com.scaling.libraryservice.mapBook.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;

/**
 * 대출 가능한 도서관 응답 데이터.
 */
@Getter @Setter
@ToString
public class RespMapBookDto {

    private String isbn13;
    private String libCode;
    private String libNm;
    private String hasBook;
    private String loanAvailable;
    private Double libLo;
    private Double libLa;
    private String libArea;
    private String libUrl;
    private Integer areaCd;

    private Boolean available;

    public RespMapBookDto(ApiBookExistDto dto, LibraryDto libraryDto) {

        this.isbn13 = dto.getIsbn13();
        this.libCode = dto.getLibCode();
        this.hasBook = dto.getHasBook();
        this.loanAvailable = dto.getLoanAvailable();

        this.libLo = libraryDto.getLibLon();
        this.libLa = libraryDto.getLibLat();
        this.libArea = libraryDto.getLibArea();
        this.libNm = libraryDto.getLibNm();
        this.libUrl = libraryDto.getLibUrl();
        this.areaCd = libraryDto.getAreaCd();

        this.available = true;

    }

    public RespMapBookDto(ReqMapBookDto reqMapBookDto,LibraryDto libraryDto) {

        this.isbn13 = reqMapBookDto.getIsbn();
        this.libCode = String.valueOf(libraryDto.getLibNo());
        this.loanAvailable = "N";

        this.hasBook = libraryDto.getHasBook();
        this.libLo = libraryDto.getLibLon();
        this.libLa = libraryDto.getLibLat();
        this.libArea = libraryDto.getLibArea();
        this.libNm = libraryDto.getLibNm();
        this.libUrl = libraryDto.getLibUrl();
        this.areaCd = libraryDto.getAreaCd();

        this.available = false;
    }

    public RespMapBookDto(JSONObject jsonObject){

        this.isbn13 = jsonObject.getString("isbn13");
        this.libCode = jsonObject.getString("libCode");
        this.hasBook = jsonObject.getString("hasBook");
        this.loanAvailable = jsonObject.getString("loanAvailable");

        this.libLo = jsonObject.getDouble("libLo");
        this.libLa = jsonObject.getDouble("libLa");
        this.libArea = jsonObject.getString("libArea");
        this.libNm = jsonObject.getString("libNm");
        this.libUrl = jsonObject.getString("libUrl");
        this.areaCd = jsonObject.getInt("areaCd");

        this.available = jsonObject.getBoolean("available");
    }



}
