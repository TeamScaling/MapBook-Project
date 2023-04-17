package com.scaling.libraryservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter @Setter @ToString
public class ReqMapBookDto {

    private final String isbn;
    private final double lat;

    private final double lon;
    private final String oneArea;
    private final String twoArea;

    public boolean isAddressRequest(){

        return oneArea != null & twoArea != null;
    }

}
