package com.scaling.libraryservice.search.util;


import static com.scaling.libraryservice.search.util.SearchMode.BooleanMode;
import static com.scaling.libraryservice.search.util.SearchMode.NaturalMode;

/**
 * 검색어 유형을 정의하기 위한 Enum 클래스입니다.
 */
public enum TitleType {

    TOKEN_ONE(NaturalMode),

    TOKEN_TWO_OR_MORE(BooleanMode);


    private final SearchMode mode;

    TitleType(SearchMode mode) {
        this.mode = mode;
    }

    public SearchMode getMode() {
        return mode;
    }
}
