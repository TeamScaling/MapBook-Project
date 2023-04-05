package com.scaling.libraryservice.search.dto;


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
}
