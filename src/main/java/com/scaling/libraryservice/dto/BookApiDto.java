package com.scaling.libraryservice.dto;

import lombok.Getter;
import lombok.ToString;
import org.json.JSONObject;

@Getter @ToString
public class BookApiDto {

    private final String isbn13;

    private final String libCode;

    private final String hasBook;

    private final String loanAvailable;

    public BookApiDto(JSONObject req, JSONObject result){

        this.isbn13 = req.getString("isbn13");
        this.libCode = req.getString("libCode");
        this.hasBook = result.getString("hasBook");
        this.loanAvailable = result.getString("loanAvailable");
    }


}
