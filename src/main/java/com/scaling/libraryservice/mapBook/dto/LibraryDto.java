package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import com.scaling.libraryservice.mapBook.entity.Library;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

// Library entity를 담는 dto
@Getter
@Setter
@ToString
@Slf4j
public class LibraryDto implements ConfigureUriBuilder {

    private String libNm;

    private Integer libNo;

    private Double libLon;

    private Double libLat;

    private String libArea;

    private String libUrl;

    private String oneAreaNm;

    private String twoAreaNm;

    private Integer areaCd;

    private static final String API_URL = "http://data4library.kr/api/bookExist";
    private static final String AUTH_KEY = "55db267f8f05b0bf8e23e8d3f65bb67d206a6b5ce24f5e0ee4625bcf36e4e2bb";

    public LibraryDto(Library library) {
        this.libNm = library.getLibNm();
        this.libNo = library.getLibNo();
        this.libLon = library.getLibLon();
        this.libLat = library.getLibLat();
        this.libArea = library.getLibArea();
        this.libUrl = library.getLibUrl();
        this.oneAreaNm = library.getOneAreaNm();
        this.twoAreaNm = library.getTwoAreaNm();
        this.areaCd = library.getAreaCd();
    }

    public LibraryDto(Integer libNo) {
        this.libNo = libNo;
    }

    public String getFullAreaNm() {

        return this.oneAreaNm + " " + this.twoAreaNm;
    }


    @Override
    public UriComponentsBuilder configUriBuilder(String target) {

        return UriComponentsBuilder.fromHttpUrl(API_URL)
            .queryParam("authKey", AUTH_KEY)
            .queryParam("isbn13", target)
            .queryParam("libCode", String.valueOf(this.libNo));
    }

    @Override
    public String getApiUrl() {
        return API_URL;
    }
}
