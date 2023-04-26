package com.scaling.libraryservice.mapBook.entity;

import java.util.Objects;

public class BookSet {

    private String isbn;
    private String title;
    private String authr;
    private String publisher;
    private String image;

    private String content;

    public BookSet(String isbn, String title, String authr, String publisher, String image,String content) {
        this.isbn = isbn;
        this.title = title;
        this.authr = authr;
        this.publisher = publisher;
        this.image = image;
        this.content = content;
    }

    public String joinString(){
       return String.join(",",isbn,title,authr,publisher,image,content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookSet bookSet = (BookSet) o;
        return Objects.equals(isbn, bookSet.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
