package com.scaling.libraryservice.mapBook.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.json.JSONObject;

@Getter
@RequiredArgsConstructor
@ToString
public class LoanItemDto {

    private final Integer no;

    private final Integer ranking;

    private final String bookName;

    private final Double isbn13;

    private final Integer loan_count;

    public LoanItemDto(JSONObject obj) {

        this.no = obj.getInt("no");
        this.ranking = Integer.parseInt(obj.getString("ranking"));
        this.bookName = obj.getString("bookname");
        this.isbn13 = Double.parseDouble(obj.getString("isbn13"));
        this.loan_count = Integer.parseInt(obj.getString("loan_count"));
    }

    public static Map<String, String> createParamMap(int pageSize) {
        Map<String,String> paramMap = new HashMap<>();

        paramMap.put("apiUri","http://data4library.kr/api/loanItemSrch");
        paramMap.put("pageSize", String.valueOf(pageSize));
        paramMap.put("format", "json");

        return paramMap;
    }
}
