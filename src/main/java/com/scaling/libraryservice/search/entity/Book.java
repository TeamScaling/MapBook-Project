package com.scaling.libraryservice.search.entity;

import com.scaling.libraryservice.batch.bookUpdate.entity.RequiredUpdateBook;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;


@Entity
@Table(name = "book_final")
@ToString
@Getter @Setter
@Builder @AllArgsConstructor
public class Book {

    @Id
    @Column(name = "id_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "title_token",nullable = true)
    private String titleToken;

    @Column(name = "pblicte_de")
    private String publishDate;

    @Column(name = "loan_cnt")
    private Integer loanCnt;


    public Integer getLoanCnt() {
        return loanCnt == null ? 0 : this.loanCnt;
    }

    @Nullable
    public String getTitleToken() {
        return titleToken == null? "" : this.titleToken;
    }

    public void setLoanCnt(Integer loanCnt) {
        this.loanCnt = loanCnt;
    }

    public Book() {
    }

    public Book(RequiredUpdateBook requiredUpdateBook) {
        this.title = requiredUpdateBook.getTitle();
        this.content =  requiredUpdateBook.getContent();
        this.author = requiredUpdateBook.getAuthor();
        this.isbn = requiredUpdateBook.getIsbn();
        this.bookImg = requiredUpdateBook.getBookImg();
        this.loanCnt = requiredUpdateBook.getLoanCnt();
        this.publishDate = requiredUpdateBook.getPublishDate();
    }
}
