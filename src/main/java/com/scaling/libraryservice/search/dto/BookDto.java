package com.scaling.libraryservice.search.dto;

import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.engine.util.TitleTrimmer;
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

    private Integer loanCnt;


    public BookDto(@NonNull Book book) {
        this.id = book.getId();
        this.title = TitleTrimmer.TrimTitleResult(book.getTitle());
        this.content =  book.getContent().isEmpty() ? "[내용 없음]" : book.getContent();
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
        this.bookImg = book.getBookImg().isEmpty() ?
            "https://someone-be-bucket.s3.ap-northeast-2.amazonaws.com/mapbook+logo.png"
            : book.getBookImg();
        this.loanCnt = book.getLoanCnt();
    }

    public static BookDto emptyDto(){

        return new BookDto(0L,"","","","","",0);
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
