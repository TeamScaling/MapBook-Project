package com.scaling.libraryservice.search.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class RespRecommend {

    private final List<RecommendBookDto> recommendBooks;

    public RespRecommend(List<RecommendBookDto> recommendBooks) {
        this.recommendBooks = recommendBooks;
    }
}
