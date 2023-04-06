package com.scaling.libraryservice.search.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity
public class Book {

    @Id
    private Long id;

    private String title_nm;

    private Integer seq_no;

    private String authr_nm;

    private String book_intrcn_cn;

    private String publisher_nm;

    private String image_url;

    private String isbn;

    public Book (String title_nm, String authr_nm, Integer seq_no){
        this.title_nm = title_nm;
        this.authr_nm = authr_nm;
        this.seq_no = seq_no;
    }


}
