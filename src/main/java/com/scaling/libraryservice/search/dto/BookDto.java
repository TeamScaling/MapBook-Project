package com.scaling.libraryservice.search.dto;

import com.scaling.libraryservice.search.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

@Getter
@Setter @ToString
@Builder @AllArgsConstructor
public class BookDto {

    private final Long id;

    private final String title;

    private final String content;

    private final String author;

    private final String isbn;

    private String bookImg;


    public BookDto(@NonNull Book book) {

        this.id = book.getId();
        this.title = book.getTitle();

        if (book.getContent().isEmpty()) {
            this.content = "[내용 없음]";
        } else {
            this.content = book.getContent();
        }
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
        this.bookImg =book.getBookImg();
    }


    public BookDto(@NonNull BookDto bookDto) {
        this.id = bookDto.getId();
        this.title = bookDto.getTitle();
        this.content = bookDto.getContent();
        this.author = bookDto.getAuthor();
        this.isbn = bookDto.getIsbn();
        this.bookImg = bookDto.getBookImg();
    }

    public BookDto(@NonNull JSONObject json) {
        this.id = json.getLong("id");
        this.title = json.getString("title");
        this.content = json.getString("content");
        this.author = json.getString("author");
        this.isbn = json.getString("isbn");
        this.bookImg = json.getString("bookImg");

    }



}
