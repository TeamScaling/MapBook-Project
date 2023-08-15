package com.scaling.libraryservice.batch.bookUpdate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.lang.NonNull;


@ToString
@Getter @Builder @AllArgsConstructor
public class BookApiDto {

    private String isbn;

    private String authors;

    private String publisher;

    private String thumbnail;

    private String contents;

    private String dateTime;

    private String title;

    public BookApiDto(@NonNull JSONObject jsonObj){
        this.isbn = jsonObj.getString("isbn").split(" ")[1];
        this.authors = jsonObj.getJSONArray("authors").join(",");
        this.publisher = jsonObj.getString("publisher");
        this.thumbnail = jsonObj.getString("thumbnail");
        this.contents = jsonObj.getString("contents");
        this.dateTime = jsonObj.getString("datetime").split("T")[0];
        this.title = jsonObj.getString("title");
    }

    public BookApiDto() {
    }
}
