package com.scaling.libraryservice.mapBook.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@Table(name = "lib_hasbook")
@ToString(exclude = "library")
public class LibraryHasBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ISBN")
    private Double isbn13;

    @Column(name = "LOAN_CNT")
    private Integer loanCnt;

    @ManyToOne
    @JoinColumn(name = "LBRRY_NO")
    private Library library;

    @Column(name = "REGIS_DATA")
    private LocalDate regisDate;

    @Column(name = "area_cd")
    private Integer areaCd;

}
