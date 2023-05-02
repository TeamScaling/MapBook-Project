package com.scaling.libraryservice.search.domain;

/**
 * 검색어 유형을 정의하기 위한 Enum 클래스입니다.
 */
public enum TitleType {
    /** 한국어 단일 단어 검색어 유형 */
    KOR_SG,

    /** 영어 단일 단어 검색어 유형 */
    ENG_SG,

    /** 한국어 다중 단어 검색어 유형 */
    KOR_MT,

    /** 영어 다중 단어 검색어 유형 */
    ENG_MT,

    /** 한국어-영어 혼합 검색어 유형 */
    KOR_ENG,

    /** 영어-한국어 단일 단어 검색어 유형 */
    ENG_KOR_SG,

    /** 영어-한국어 다중 단어 검색어 유형 */
    ENG_KOR_MT
}
