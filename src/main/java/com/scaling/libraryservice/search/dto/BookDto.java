package com.scaling.libraryservice.search.dto;

import com.scaling.libraryservice.search.entity.Book;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {

    private Integer seqId;

    private String title;

    private String content;

    private String author;

    private String isbn;

    private String bookImg;

    private String kdcNm;

    private String relatedTitle;

    public BookDto(String relatedTitle) {
        this.relatedTitle = relatedTitle;
    }


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

        if (book.getBookImg().isEmpty()) {
            this.bookImg = "[내용 없음]";
        } else {
            this.bookImg = book.getBookImg();
        }

        this.kdcNm = book.getKdcNm();
    }


    @Override
    public String toString() {
        return "BookDto{" +
            ", title='" + title + '\'' +
            ", isbn='" + isbn + '\'' +
            '}';
    }


    //중복제거
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        BookDto bookDto = (BookDto) o;
//        return Objects.equals(title, bookDto.title);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(title);
//    }


}
