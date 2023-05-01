package com.scaling.libraryservice.recommend.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import com.scaling.libraryservice.commons.caching.CustomCacheable;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.recommend.dto.RespRecommend;
import com.scaling.libraryservice.recommend.repository.RecommendRepository;
import com.scaling.libraryservice.recommend.util.RecommendRule;
import com.scaling.libraryservice.search.domain.TitleQuery;
import com.scaling.libraryservice.search.domain.TitleType;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.util.TitleAnalyzer;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRule recommendRule;

    private final RecommendRepository recommendRepo;

    private final TitleAnalyzer titleAnalyzer;

    private final CustomCacheManager<List<String>> cacheManager;

    @PostConstruct
    private void init() {

        Cache<CacheKey, List<String>> bookCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

        cacheManager.registerCaching(bookCache, this.getClass());
    }

    @Timer
    public RespRecommend getRecommendBook2(RespBooksDto searchResult) {
        return recommendRule.recommendBooks(searchResult);
    }

    /**
     * 검색어를 가지고 그에 맞는 추천 도서 제목 목록을 반환 한다.
     *
     * @param query 추천 받고자 하는 검색어 문자열
     * @return 추천 도서 제목 문자열을 담고 있는 List
     */
    @Timer
    @CustomCacheable
    public List<String> getRecommendBook(String query) {

        return pickSelectQuery(query, 5).stream().map(r -> getRelatedTitle(r.getTitle())).toList();
    }

    /**
     * 추천 도서 기반이 되는 DB에 검색어에 따라 좋은 성능과 최적의 결과를 얻을 수 있는 쿼리를 선택 하고, 추천 도서 데이터를 반환 한다.
     *
     * @param query 추천 받고자 하는 검색어
     * @param size  추천 도서를 어느 범위까지 보여 줄지에 대한 값
     * @return 선택된 추천 도서 DTO들을 담은 List
     */
    public List<BookDto> pickSelectQuery(String query, int size) {

        TitleQuery titleQuery = titleAnalyzer.analyze(query);

        TitleType type = titleQuery.getTitleType();

        switch (type) {

            case KOR_SG -> {
                return recommendRepo.findBooksByKorBoolOrder(titleQuery.getKorToken(), size)
                    .stream().map(BookDto::new).toList();
            }

            case KOR_MT -> {
                return recommendRepo.findBooksByKorMtFlexible(titleQuery.getKorToken(), size)
                    .stream().map(BookDto::new).toList();
            }

            case ENG_SG -> {
                return recommendRepo.findBooksByEngBoolOrder(titleQuery.getEngToken(), size)
                    .stream().map(BookDto::new).toList();
            }
            case ENG_MT -> {
                return recommendRepo.findBooksByEngMtOrderFlexible(titleQuery.getEngToken(), size)
                    .stream().map(BookDto::new).toList();
            }
            case KOR_ENG -> {
                return recommendRepo.findBooksByKorNaturalOrder(titleQuery.getEngKorToken(), size)
                    .stream().map(BookDto::new).toList();
            }
            case ENG_KOR_SG -> {
                return recommendRepo.findBooksByEngKorBoolOrder(
                    titleQuery.getEngToken(),
                    titleQuery.getKorToken(),
                    size).stream().map(BookDto::new).toList();
            }

            case ENG_KOR_MT -> {
                return recommendRepo.findBooksByEngKorNaturalOrder(
                    titleQuery.getEngToken(),
                    titleQuery.getKorToken(),
                    size).stream().map(BookDto::new).toList();
            }
        }

        return null;
    }

    private String getRelatedTitle(String title) {
        String[] titleParts = title.split(":");
        if (titleParts.length > 1) {
            String titlePrefix = titleParts[0];
            String[] titlePrefixParts = titlePrefix.trim().split("=");
            if (titlePrefixParts.length > 0) {
                titlePrefix = titlePrefixParts[0].trim();
            }
            return removeParentheses(removeDash(titlePrefix)).trim();
        }
        return removeParentheses(removeDash(title)).trim();
    }

    private String removeParentheses(String text) {
        return text.replaceAll("\\(.*?\\)|=.*$", "").trim();
    }

    private String removeDash(String text) {
        return text.replaceAll("-.*$", "").trim();
    }

}
