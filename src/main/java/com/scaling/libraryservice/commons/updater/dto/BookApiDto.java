package com.scaling.libraryservice.commons.updater.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;

@Setter @Getter
@ToString
public class BookApiDto {

    private String isbn;

    private String authors;

    private String publisher;

    private String thumbnail;

    private String contents;

    private String dateTime;

    private String title;

    public BookApiDto(JSONObject jsonObj){
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
