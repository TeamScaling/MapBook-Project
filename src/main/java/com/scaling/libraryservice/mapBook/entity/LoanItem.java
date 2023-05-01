package com.scaling.libraryservice.mapBook.entity;

import com.scaling.libraryservice.mapBook.dto.LoanItemDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 인기 도서 테이블과 맵핑 된다.
 */
@Entity
@Setter @Getter
public class LoanItem extends TimeStamp {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false)
    private Integer ranking;

    @Column(name = "ISBN_THIRTEEN_NO",nullable = false)
    private Double isbn13;

    @Column(nullable = false)
    private Integer loan_count;

    @Column(name = "class_no")
    private String classNo;

    public LoanItem(LoanItemDto dto) {
        this.ranking = dto.getRanking();
        this.isbn13 = dto.getIsbn13();
        this.loan_count = dto.getLoan_count();
        this.classNo = dto.getClassNo();
    }

    public LoanItem() {

    }
}
