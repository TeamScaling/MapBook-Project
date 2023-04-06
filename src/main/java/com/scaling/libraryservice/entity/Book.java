package com.scaling.libraryservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.ToString;

//consider : 책은 무엇인가??
@Entity
@Table(name = "books")
@ToString
@Getter
public class Book {

    @Id
    @Column(name = "SEQ_NO")
    private Integer seqId;

    @Column(name = "TITLE_NM")
    private String title;

    @Column(name = "BOOK_INTRCN_CN")
    private String content;

    @Column(name = "AUTHR_NM")
    private String author;
}
