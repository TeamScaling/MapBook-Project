package com.scaling.libraryservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.ToString;

@Entity
@Table(name = "lib_info")
@ToString
public class Library {

    @Id @Column(name = "LBRRY_CD")
    private Integer libCd;

    @Column(name = "LBRRY_NM")
    private String libNm;

    @Column(name = "LBRRY_NO")
    private Integer libNo;

    @Column(name = "LBRRY_LO")
    private Double libLo;

    @Column(name = "LBRRY_LA")
    private Double libLa;

}
