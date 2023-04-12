package com.scaling.libraryservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReqBookMapDto {

    private final String isbn;

    public ReqBookMapDto(String isbn) {
        this.isbn = isbn;
    }
}
