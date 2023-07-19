package com.scaling.libraryservice.search.util;


import static com.scaling.libraryservice.search.util.SearchMode.BOOLEAN_MODE;
import static com.scaling.libraryservice.search.util.SearchMode.NATURAL_MODE;
import static com.scaling.libraryservice.search.util.SearchMode.NGRAM_MODE;

/**
 * 검색어 유형을 정의하기 위한 Enum 클래스입니다.
 */
public enum TitleType {

    TOKEN_ONE(NATURAL_MODE),

    TOKEN_TWO_OR_MORE(BOOLEAN_MODE),

    NGRAM(NGRAM_MODE);


    private final SearchMode mode;

    TitleType(SearchMode mode) {
        this.mode = mode;
    }

    public SearchMode getMode() {
        return mode;
    }
}
