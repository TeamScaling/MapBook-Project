package com.scaling.libraryservice.search.dto;

import com.scaling.libraryservice.search.entity.Book;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter @ToString
public class BookDto {

    private final Long id;

    private final String title;

    private final String content;

    private final String author;

    private final String isbn;

    private String bookImg;


    public BookDto(Book book) {

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

        if (book.getBookImg().isEmpty()) {
            this.bookImg = "[내용 없음]";
        } else {
            this.bookImg = book.getBookImg();
        }
    }

}
