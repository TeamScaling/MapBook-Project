package com.scaling.libraryservice.search.dto;

import com.scaling.libraryservice.search.dto.Meta;
import com.scaling.libraryservice.search.entity.Book;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class SearchResponseDto {

//    private Meta meta;

    private List<Book> documents = new ArrayList<>();

    public SearchResponseDto(List<Book> bookList) {
//        documents.addAll(bookList);
        this.documents = new ArrayList<>(bookList);
    }

}