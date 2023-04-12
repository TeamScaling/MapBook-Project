package com.scaling.libraryservice.dto;

import com.scaling.libraryservice.entity.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {

    private final Integer seqId;

    private final String title;

    private final String content;

    private final String author;

    private final String isbn;


    public BookDto(Book book) {

        this.seqId = book.getSeqId();
        this.title = book.getTitle();

        if (book.getContent().isEmpty()) {
            this.content = "[내용 없음]";
        } else {
            this.content = book.getContent();
        }
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
    }

}
