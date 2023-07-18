package com.scaling.libraryservice.commons.data;


import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
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

    // 출판사 이름
    @CsvBindByName(column = "PUBLISHER_NM")
    private String publisherNm;

    // 출판일
    @CsvBindByName(column = "PBLICTE_DE")
    private String pblicteDe;

    // 이미지 URL
    @CsvBindByName(column = "IMAGE_URL")
    private String imageUrl;

    // 책 소개
    @CsvBindByName(column = "BOOK_INTRCN_CN")
    private String bookIntrcnCn;

    // KDC(한국십진분류법) 이름
    @CsvBindByName(column = "KDC_NM")
    private String kdcNm;

    // 두 번째 출판일
    @CsvBindByName(column = "TWO_PBLICTE_DE")
    private String twoPblicteDe;

    // ID 번호
    @CsvBindByName(column = "id_no")
    private String idNo;

    // 대출 횟수
    @CsvBindByName(column = "loan_cnt")
    private Integer loanCnt;



}
