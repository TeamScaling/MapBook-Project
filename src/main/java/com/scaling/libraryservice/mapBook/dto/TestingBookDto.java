package com.scaling.libraryservice.mapBook.dto;

import lombok.ToString;
import org.json.JSONObject;

@ToString
public class TestingBookDto {

    private String bookName;

    public TestingBookDto() {
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public TestingBookDto(JSONObject obj) {

        this.bookName = obj.getString("bookname");
    }

    public TestingBookDto(String bookName) {
        this.bookName = bookName;
    }
}
