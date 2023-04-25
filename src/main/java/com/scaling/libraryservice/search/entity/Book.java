package com.scaling.libraryservice.search.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.ToString;


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

    @Column(name = "ISBN_THIRTEEN_NO")
    private String isbn;

    @Column(name = "IMAGE_URL")
    private String bookImg;

    @Column(name = "KDC_NM")
    private String kdcNm;

//    public String getRelatedTitle() {
//        String[] titleParts = this.title.split(":");
//        if (titleParts.length > 1) {
//            String titlePrefix = titleParts[0];
//            return titlePrefix.trim();
//        }
//        return this.title;
//    }

    //: 과 = 부분 전처리

    public String getRelatedTitle() {
        String[] titleParts = this.title.split(":");
        if (titleParts.length > 1) {
            String titlePrefix = titleParts[0];
            return titlePrefix.trim().split("=")[0];
        }
        return this.title;
    }

}
