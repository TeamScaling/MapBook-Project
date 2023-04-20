package com.scaling.libraryservice.mapBook.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity @Table(name = "ranks")
@Getter @ToString
public class Ranks {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "RANK_CO")
    private Integer rank;

    @Column(name = "ISBN_THIRTEEN_NO")
    private String isbn13;

    @Column(name = "authr_nm")
    private String authr;

    @Column(name = "title_nm")
    private String title;

}
