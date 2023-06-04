package com.scaling.libraryservice.recommend.service;

import com.scaling.libraryservice.commons.caching.CustomCacheable;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.recommend.dto.ReqRecommendDto;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.util.TitleQuery;
import com.scaling.libraryservice.search.service.BookFinder;
import com.scaling.libraryservice.search.util.TitleAnalyzer;
import com.scaling.libraryservice.search.util.TitleTrimmer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * 검색어를 기반으로 도서 추천 서비스를 제공하는 클래스 입니다. 입력된 검색어를 분석하여 추천 알고리즘을 적용하고, 추천된 도서를 DB에서 조회합니다. 조회된 도서 제목은
 * 불용어 제거를 거쳐 결과를 반환합니다.
 * <p>
 * 추천된 도서 정보는 캐시를 사용하여 성능을 향상시킵니다.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RecommendService {

    private final TitleAnalyzer titleAnalyzer;
    private final BookFinder<List<BookDto>, Integer> bookFinder;


    /**
     * 검색어를 입력받아 해당하는 추천 도서 제목 목록을 반환합니다. 결과는 불용어를 제거한 형태로 반환됩니다.
     *
     * @param reqRecommendDto 추천 받고자 하는 검색어 문자열을 담은 캐시 키가 될 수 있는 객체
     * @return 추천 도서 제목 문자열을 담고 있는 List
     */
    @Timer
    @CustomCacheable
    public List<String> getRecommendBook(@NonNull ReqRecommendDto reqRecommendDto) {

        TitleQuery titleQuery = titleAnalyzer.analyze(reqRecommendDto.getQuery());

        return bookFinder.findBooks(titleQuery, 5).stream()
            .map(r -> TitleTrimmer.TrimTitleResult(r.getTitle()))
            .toList();
    }
}
