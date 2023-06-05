package com.scaling.libraryservice.mapBook.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;


/**
 *  전국 도서관 통계 데이터. 지역별로 도서관 분포를 보여준다.
 */
@Entity
@Table(name = "lib_meta")
@Getter
public class LibraryMeta {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "AREA_CD")
    private Integer areaCd;

    @Column(name = "count")
    private Long count;

    @Column(name = "ONE_AREA_NM")
    private String oneArea;

    @Column(name = "TWO_AREA_NM")
    private String twoArea;

}
