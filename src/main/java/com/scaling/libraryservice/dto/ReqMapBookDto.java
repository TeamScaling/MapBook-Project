package com.scaling.libraryservice.dto;

import com.scaling.libraryservice.util.Location;
import com.scaling.libraryservice.util.LocationImp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter @Setter @ToString
public class ReqMapBookDto {

    private String isbn;
    private double lat;

    private double lon;

    private String oneArea;

    private String twoArea;

    public Location bindLocation(){

        return new LocationImp(this.lat,this.lon);
    }

}
