package com.scaling.libraryservice.recommend.util;

import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.recommend.dto.RespRecommend;

/**
 * 도서 추천 서비스에 사용되는 알고리즘을 정의한 인터페이스입니다.
 * 추천 알고리즘 구현체는 이 인터페이스를 구현해야 합니다.
 */
public interface RecommendRule {

    /**
     * 주어진 검색 결과에 대한 추천 도서를 반환하는 메서드입니다.
     * 구현체는 이 메서드를 통해 추천 알고리즘을 구현해야 합니다.
     *
     * @param searchResult 검색 결과 데이터를 담고 있는 RespBooksDto 객체
     * @return 추천 도서 정보를 담고 있는 RespRecommend 객체
     */
    RespRecommend recommendBooks(RespBooksDto searchResult);

}
