package com.scaling.libraryservice.dto;

import com.scaling.libraryservice.entity.Library;
import com.scaling.libraryservice.util.CreatableParamMap;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LibraryDto implements CreatableParamMap{

    private String libNm;

    private Integer libNo;

    private Double libLon;

    private Double libLat;

    private String libArea;

    private String libUrl;

    private String oneAreaNm;

    private String twoAreaNm;

    public LibraryDto(Library library) {
        this.libNm = library.getLibNm();
        this.libNo = library.getLibNo();
        this.libLon = library.getLibLon();
        this.libLat = library.getLibLat();
        this.libArea = library.getLibArea();
        this.libUrl = library.getLibUrl();
        this.oneAreaNm = library.getOneAreaNm();
        this.twoAreaNm = library.getTwoAreaNm();
    }

    @Override
    public Map<String, String> createParamMap(String target) {

        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("libCode", String.valueOf(this.libNo));
        paramMap.put("isbn13", target);

        return paramMap;
    }

}
