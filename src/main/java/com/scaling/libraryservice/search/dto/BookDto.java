package com.scaling.libraryservice.search.dto;

import com.scaling.libraryservice.search.entity.Book;
import java.util.Objects;
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

    private String kdcNm;


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
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookDto bookDto = (BookDto) o;
        return Objects.equals(title, bookDto.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }


    public String getRelatedTitle() {
        String[] titleParts = this.title.split(":");
        if (titleParts.length > 1) {
            String titlePrefix = titleParts[0];
            String[] titlePrefixParts = titlePrefix.trim().split("=");
            if (titlePrefixParts.length > 0) {
                titlePrefix = titlePrefixParts[0].trim();
            }
            return removeParentheses(removeDash(titlePrefix)).trim();
        }
        return removeParentheses(removeDash(this.title)).trim();
    }

    private String removeParentheses(String text) {
        return text.replaceAll("\\(.*?\\)|=.*$", "").trim();
    }

    private String removeDash(String text) {
        return text.replaceAll("-.*$", "").trim();
    }







}
