package com.scaling.libraryservice.dto;

import com.scaling.libraryservice.entity.Library;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class RespBookMapDto {

    private final String isbn13;
    private final String libCode;
    private final String libNm;
    private final String hasBook;
    private final String loanAvailable;
    private final Double libLo;
    private final Double libLa;
    private final String libArea;
    private final String libUrl;

    public RespBookMapDto(BookApiDto dto, Library library) {

        this.isbn13 = dto.getIsbn13();
        this.libCode = dto.getLibCode();
        this.hasBook = dto.getHasBook();
        this.loanAvailable = dto.getLoanAvailable();

        this.libLo = library.getLibLo();
        this.libLa = library.getLibLa();
        this.libArea = library.getLibArea();
        this.libNm = library.getLibNm();
        this.libUrl = library.getLibUrl();

    }
}
