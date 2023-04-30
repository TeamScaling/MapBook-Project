package com.scaling.libraryservice.search.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TitleDto {
    private List<String> titles;

    public TitleDto() {}

    public TitleDto(List<String> titles) {
        this.titles = titles;
    }

    //dto에 담긴 책 제목 확인하기

    @Override
    public String toString() {
        return "TitleDto{" +
            "titles=" + titles +
            '}';
    }


}

