package com.scaling.libraryservice.dto;

import com.scaling.libraryservice.entity.Book;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookDto {

    private final Integer seqId;

    private final String title;

    private final String content;


    public BookDto(Book book){

        this.seqId = book.getSeqId();
        this.title = book.getTitle();

        if (book.getContent() == null){
            this.content = "";
        }else{
            this.content = book.getContent();
        }
    }

}
