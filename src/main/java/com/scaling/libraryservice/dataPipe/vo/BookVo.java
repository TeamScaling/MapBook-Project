package com.scaling.libraryservice.dataPipe.vo;


import com.opencsv.bean.CsvBindByName;
import com.scaling.libraryservice.search.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookVo {

    // ISBN-13
    @CsvBindByName(column = "ISBN_THIRTEEN_NO")
    private String isbnThirteenNo;

    // 책 제목
    @CsvBindByName(column = "TITLE_NM")
    private String titleNm;

    // 저자 이름
    @CsvBindByName(column = "AUTHR_NM")
    private String authrNm;

    // 출판일
    @CsvBindByName(column = "PBLICTE_DE")
    private String publishDate;

    // 이미지 URL
    @CsvBindByName(column = "IMAGE_URL")
    private String imageUrl;

    // 책 소개
    @CsvBindByName(column = "BOOK_INTRCN_CN")
    private String bookIntrcn;

    // 대출 횟수
    @CsvBindByName(column = "loan_cnt")
    private Integer loanCnt;

    // 영문 제목 토큰
    @CsvBindByName(column = "TITLE_TOKEN")
    private String titleToken;

    public BookVo(Book book, String titleToken) {
        this.isbnThirteenNo = book.getIsbn();
        this.titleNm = book.getTitle();
        this.authrNm = book.getAuthor();
        this.imageUrl = book.getBookImg();
        this.bookIntrcn = book.getContent();
        this.loanCnt = book.getLoanCnt();
        this.publishDate = book.getPublishDate();
        this.titleToken = titleToken;
    }

    public BookVo(Book book) {
        this.isbnThirteenNo = book.getIsbn();
        this.titleNm = book.getTitle();
        this.authrNm = book.getAuthor();
        this.imageUrl = book.getBookImg();
        this.bookIntrcn = book.getContent();
        this.loanCnt = book.getLoanCnt();
        this.publishDate = book.getPublishDate();
        this.titleToken = book.getTitleToken();
    }

    public BookVo(String authrTitleToken, Book book) {
        this.isbnThirteenNo = book.getIsbn();
        this.titleNm = book.getTitle();
        this.authrNm = book.getAuthor();
        this.imageUrl = book.getBookImg();
        this.bookIntrcn = book.getContent();
        this.loanCnt = book.getLoanCnt();
        this.publishDate = book.getPublishDate();
        this.titleToken = authrTitleToken;
    }


}
