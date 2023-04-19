package com.scaling.libraryservice.mapBook.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.json.JSONObject;

// OpenAPI의 대출 가능 여부 응답을 담는 객체.
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
