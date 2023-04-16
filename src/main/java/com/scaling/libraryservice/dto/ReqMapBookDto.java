package com.scaling.libraryservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter @Setter @ToString
public class ReqMapBookDto {

    private String isbn;

    private String lat;

    private String lon;

    private String area = "성남";

}
