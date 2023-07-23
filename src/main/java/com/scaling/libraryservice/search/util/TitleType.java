package com.scaling.libraryservice.search.util;


import static com.scaling.libraryservice.search.util.SearchMode.BOOLEAN_MODE;
import static com.scaling.libraryservice.search.util.SearchMode.NATURAL_MODE;

/**
 * 검색어 유형을 정의하기 위한 Enum 클래스입니다.
 */
public enum TitleType {

    TOKEN_ONE(NATURAL_MODE,null),

    TOKEN_TWO_OR_MORE(BOOLEAN_MODE,null),
    TOKEN_COMPLEX(BOOLEAN_MODE,NATURAL_MODE),

    TOKEN_ALL_ETC(NATURAL_MODE,null);


    private final SearchMode mode;

    private final SearchMode secondMode;

    TitleType(SearchMode mode,SearchMode secondMode) {
        this.mode = mode;
        this.secondMode = secondMode;
    }

    public SearchMode getMode() {
        return mode;
    }

    public SearchMode getSecondMode(){
        if(secondMode == null){
            throw new NullPointerException();
        }

        return secondMode;
    }
}
