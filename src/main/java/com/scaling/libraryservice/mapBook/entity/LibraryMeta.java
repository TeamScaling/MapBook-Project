package com.scaling.libraryservice.mapBook.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Entity
@Table(name = "lib_meta")
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
