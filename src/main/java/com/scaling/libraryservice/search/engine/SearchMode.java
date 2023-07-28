package com.scaling.libraryservice.search.engine;

// match against절에서 against에 들어갈 mode를 결정
public enum SearchMode {
    BOOLEAN_MODE,
    NATURAL_MODE,

    //명사와 다른 어절을 함께 고려해서 검색
    //실제 against절에 complex_mode로 들어가진 않는다.
    COMPLEX_MODE
}