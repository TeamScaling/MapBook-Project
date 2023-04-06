package com.scaling.libraryservice.search.dto;

import com.scaling.libraryservice.search.entity.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class SearchResponseDto {

//    private Meta meta;

    private int bookCnt;

    private List<Book> documents = new ArrayList<>();

    public SearchResponseDto(List<Book> bookList, int bookCnt) {
//        documents.addAll(bookList);
        this.bookCnt = bookCnt;

        this.documents = new ArrayList<>(bookList);

    }
}