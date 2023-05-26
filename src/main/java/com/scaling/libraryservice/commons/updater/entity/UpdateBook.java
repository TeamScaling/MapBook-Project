package com.scaling.libraryservice.commons.updater.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@Table(name = "out_data_book")
@ToString
public class UpdateBook {

    @Id
    @Column(name = "id_no")
    private Long id;

    @Column(name = "ISBN_THIRTEEN_NO")
    private String isbn;

    @Column(name = "TITLE_NM")
    private String title;

    @Column(name = "BOOK_INTRCN_CN")
    private String content;

    @Column(name = "AUTHR_NM")
    private String author;

    @Column(name = "IMAGE_URL")
    private String bookImg;

    @Column(name = "TWO_PBLICTE_DE")
    private String datatime;

    @Column(name = "publisher_nm")
    private String publisher;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBookImg(String bookImg) {
        this.bookImg = bookImg;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
