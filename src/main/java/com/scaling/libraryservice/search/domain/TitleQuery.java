package com.scaling.libraryservice.search.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 검색 쿼리를 분석한 결과를 저장하기 위한 클래스입니다.
 * 각 검색어 토큰과 검색어 유형(TitleType)에 대한 정보를 저장합니다.
 */
@RequiredArgsConstructor
@ToString @Getter
public class TitleQuery {

    /** 검색어 유형 정보를 저장하는 변수 */
    private final TitleType titleType;

    /** 영어 검색어 토큰을 저장하는 변수 */
    private final String engToken;

    /** 한국어 검색어 토큰을 저장하는 변수 */
    private final String korToken;

    /** 영어와 한국어 혼합된 검색어 토큰을 저장하는 변수 */
    private final String engKorToken;

}
