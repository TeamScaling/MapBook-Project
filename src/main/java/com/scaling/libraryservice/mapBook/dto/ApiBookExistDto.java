package com.scaling.libraryservice.mapBook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

/**
 *  OpenAPI (대출 가능 도서) 응답 데이터를 담는 클래스
 */
@Getter @ToString
@Builder @AllArgsConstructor

public class ApiBookExistDto {

    private final String isbn13;

    private final String libCode;

    private final String hasBook;

    private final String loanAvailable;

    public ApiBookExistDto(@NonNull JSONObject req,@NonNull JSONObject result) {

        this.isbn13 = req.getString("isbn13");
        this.libCode = req.getString("libCode");
        this.hasBook = result.getString("hasBook");
        this.loanAvailable = result.getString("loanAvailable");
    }

    public boolean isLoanAble(){

        return loanAvailable.equals("Y");
    }
}
