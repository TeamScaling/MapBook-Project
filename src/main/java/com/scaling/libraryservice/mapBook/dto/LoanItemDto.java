package com.scaling.libraryservice.mapBook.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.json.JSONObject;

@Getter
@RequiredArgsConstructor
@ToString
public class LoanItemDto {

    private Integer no;

    private Integer ranking;

    private String bookName;

    private Double isbn13;

    private Integer loan_count;

    private String classNo;


    public LoanItemDto(JSONObject obj) {

        this.no = obj.getInt("no");
        this.ranking = Integer.parseInt(obj.getString("ranking"));
        this.bookName = obj.getString("bookname");
        this.isbn13 = Double.parseDouble(obj.getString("isbn13"));
        this.loan_count = Integer.parseInt(obj.getString("loan_count"));
        this.classNo = obj.getString("class_no");
    }


}
