package com.scaling.libraryservice.mapBook.dto;

import lombok.Getter;
import lombok.ToString;
import org.json.JSONObject;

/**
 *  OpenAPI (대출 가능 도서) 응답 데이터를 담는 클래스
 */
@Getter @ToString
public class ApiBookExistDto {

    private final String isbn13;

    private final String libCode;

    private final String hasBook;

    private final String loanAvailable;

    public ApiBookExistDto(JSONObject req, JSONObject result) {

        this.isbn13 = req.getString("isbn13");
        this.libCode = req.getString("libCode");
        this.hasBook = result.getString("hasBook");
        this.loanAvailable = result.getString("loanAvailable");
    }

}
