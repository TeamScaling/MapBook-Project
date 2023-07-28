package com.scaling.libraryservice.search.engine;


import static com.scaling.libraryservice.search.engine.SearchMode.BOOLEAN_MODE;
import static com.scaling.libraryservice.search.engine.SearchMode.NATURAL_MODE;

/**
 * 검색어 유형을 정의하기 위한 Enum 클래스입니다.
 */
public enum TitleType {
    
    // 명사가 하나
    TOKEN_ONE(NATURAL_MODE,null),
    
    // 명사가 두개 이상
    TOKEN_TWO_OR_MORE(BOOLEAN_MODE,null),
    
    // 명사는 존재하나 다른 어절도 필요한 상황 
    TOKEN_COMPLEX(BOOLEAN_MODE,NATURAL_MODE),
    
    // 명사가 존재하지 않을 때, 검색어로 전체 검색
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
