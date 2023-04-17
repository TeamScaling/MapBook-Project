package com.scaling.libraryservice.entity;

import com.scaling.libraryservice.util.Location;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "lib_info")
@ToString @Getter
public class Library implements Location {

    @Id @Column(name = "LBRRY_CD")
    private Integer libCd;

    @Column(name = "LBRRY_NM")
    private String libNm;

    @Column(name = "LBRRY_NO")
    private Integer libNo;

    @Column(name = "LBRRY_LO")
    private Double libLon;

    @Column(name = "LBRRY_LA")
    private Double libLat;

    @Column(name = "WETHR_AREA_CD")
    private String libArea;

    @Column(name = "HMPG_VALUE")
    private String libUrl;

    @Column(name = "ONE_AREA_NM")
    private String oneAreaNm;

    @Column(name = "TWO_AREA_NM")
    private String twoAreaNm;

    @Override
    public double getLat() {
        return libLat;
    }

    @Override
    public double getLon() {
        return libLon;
    }

    public String getTwoAreaNm(){

        return twoAreaNm.split(" ")[0];
    }

}
