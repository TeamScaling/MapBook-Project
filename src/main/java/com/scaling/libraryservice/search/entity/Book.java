package com.scaling.libraryservice.search.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "books")
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @Column(name = "id_no")
    private Long id;

    @Column(name = "TITLE_NM")
    private String title;

    @Column(name = "BOOK_INTRCN_CN")
    private String content;

    @Column(name = "AUTHR_NM")
    private String author;

    @Column(name = "ISBN_THIRTEEN_NO")
    private String isbn;

    @Column(name = "IMAGE_URL")
    private String bookImg;

    @Column(name = "loan_cnt")
    private Integer loanCnt;

    public Integer getLoanCnt() {
        return loanCnt == null ? 0 : this.loanCnt;
    }
}
