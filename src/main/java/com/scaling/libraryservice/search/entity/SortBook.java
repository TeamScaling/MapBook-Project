package com.scaling.libraryservice.search.entity;

import com.scaling.libraryservice.search.dto.BookDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "book_ordered")
@ToString
@Getter @Setter
public class SortBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "title_token")
    private String titleToken;

    @Column(name = "pblicte_de")
    private String publishDate;

    @Column(name = "loan_cnt")
    private Integer loanCnt;

    public Integer getLoanCnt() {
        return loanCnt == null ? 0 : this.loanCnt;
    }

    public void setLoanCnt(Integer loanCnt) {
        this.loanCnt = loanCnt;
    }

    public SortBook() {
    }

    public SortBook(BookDto bookDto) {
        this.title = bookDto.getTitle();
        this.content = bookDto.getContent();
        this.author = bookDto.getAuthor();
        this.isbn = bookDto.getIsbn();
        this.bookImg = bookDto.getBookImg();
        this.titleToken = bookDto.getTitleToken();
        this.publishDate = bookDto.getPublishDate();
        this.loanCnt = bookDto.getLoanCnt();
    }

}
