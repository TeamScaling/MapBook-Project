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
import com.scaling.libraryservice.search.entity.Book;
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

    @CustomCacheable
    @Timer
    public List<String> getRecommendBook(String query) {

        List<BookDto> result = pickSelectQuery(query).stream().map(BookDto::new).toList();

        return result.stream().map(r -> getRelatedTitle(r.getTitle())).toList();
    }

    public List<Book> pickSelectQuery(String query) {

        TitleQuery titleQuery = titleAnalyzer.analyze(query);

        TitleType type = titleQuery.getTitleType();

        int size = 5;

        switch (type) {

            case KOR_SG -> {
                return recommendRepo.findBooksByKorBoolOrder(titleQuery.getKorToken(), size);
            }

            case KOR_MT -> {
                return recommendRepo.findBooksByKorMtFlexible(titleQuery.getKorToken(), size);
            }

            case ENG_SG -> {
                return recommendRepo.findBooksByEngBoolOrder(titleQuery.getEngToken(), size);
            }
            case ENG_MT -> {
                return recommendRepo.findBooksByEngMtOrderFlexible(titleQuery.getEngToken(), size);
            }
            case KOR_ENG -> {
                return recommendRepo.findBooksByKorNaturalOrder(titleQuery.getEngKorToken(), size);
            }
            case ENG_KOR_SG -> {
                return recommendRepo.findBooksByEngKorBoolOrder(
                    titleQuery.getEngToken(),
                    titleQuery.getKorToken(),
                    size);
            }

            case ENG_KOR_MT -> {
                return recommendRepo.findBooksByEngKorNaturalOrder(
                    titleQuery.getEngToken(),
                    titleQuery.getKorToken(),
                    size);
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
