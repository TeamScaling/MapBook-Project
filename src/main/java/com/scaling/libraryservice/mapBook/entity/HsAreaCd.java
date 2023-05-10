package com.scaling.libraryservice.mapBook.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Getter @Table(name = "hasbook_area")
public class HsAreaCd {

    @Id
    private Integer areaCd;

}
