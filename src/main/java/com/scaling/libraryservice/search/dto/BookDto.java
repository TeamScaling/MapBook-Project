package com.scaling.libraryservice.search.dto;


import com.scaling.libraryservice.search.entity.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class BookDto {

    private String title_nm;

    private Integer seq_no;

    private String authr_nm;

    private String book_intrcn_cn;

    private String publisher_nm;

    private String image_url;

    private String isbn;

    public BookDto(Book book){
        this.title_nm = book.getTitle_nm();
        this.authr_nm = book.getAuthr_nm();
        this.publisher_nm = book.getPublisher_nm();
    }
}
