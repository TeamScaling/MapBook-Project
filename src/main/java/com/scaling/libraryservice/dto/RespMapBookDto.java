package com.scaling.libraryservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class RespMapBookDto {

    private final String isbn13;
    private final String libCode;
    private final String libNm;
    private final String hasBook;
    private final String loanAvailable;
    private final Double libLo;
    private final Double libLa;
    private final String libArea;
    private final String libUrl;

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

    }
}
