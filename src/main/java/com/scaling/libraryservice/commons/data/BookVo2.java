package com.scaling.libraryservice.commons.data;


import com.opencsv.bean.CsvBindByName;
import com.scaling.libraryservice.search.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookVo2 {
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
    private String pblicteDe;

    // 이미지 URL
    @CsvBindByName(column = "IMAGE_URL")
    private String imageUrl;

    // 책 소개
    @CsvBindByName(column = "BOOK_INTRCN_CN")
    private String bookIntrcnCn;


    // 대출 횟수
    @CsvBindByName(column = "loan_cnt")
    private Integer loanCnt;

    // 영문 제목 토큰
    @CsvBindByName(column = "ENG_TITLE_TOKEN")
    private String engTitleToken;



    public BookVo2(Book book,String engTitleToken) {
        this.isbnThirteenNo = book.getIsbn();
        this.titleNm = book.getTitle();
        this.authrNm = book.getAuthor();
        this.imageUrl = book.getBookImg();
        this.bookIntrcnCn = book.getContent();
        this.loanCnt = book.getLoanCnt();
        this.engTitleToken = engTitleToken;
    }
}
