package com.scaling.libraryservice.search.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import com.scaling.libraryservice.commons.caching.CustomCacheable;
import com.scaling.libraryservice.search.domain.TitleQuery;
import com.scaling.libraryservice.search.domain.TitleType;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.util.TitleAnalyzer;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
@Timer
@Getter
public class BookSearchService {

    private final BookRepository bookRepository;
    private final CustomCacheManager<RespBooksDto> cacheManager;
    private final TitleAnalyzer titleAnalyzer;

    @PostConstruct
    private void init() {

        Cache<CacheKey, RespBooksDto> bookCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

        cacheManager.registerCaching(bookCache, this.getClass());
    }

    @Timer
    @CustomCacheable
    public RespBooksDto searchBooks(String query, int page, int size, String target) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Book> books = pickSelectQuery(query, pageable);

        Objects.requireNonNull(books);

        return new RespBooksDto(
            new MetaDto(books.getTotalPages(),
                books.getTotalElements(), page, size),
            books.stream().map(BookDto::new).toList());
    }

    public Page<Book> pickSelectQuery(String query, Pageable pageable) {

        TitleQuery titleQuery = titleAnalyzer.analyze(query);

        TitleType type = titleQuery.getTitleType();

        switch (type) {

            case KOR_SG -> {
                return bookRepository.findBooksByKorNatural(titleQuery.getKorToken(), pageable);
            }
            case ENG_SG -> {
                return bookRepository.findBooksByEngBool(titleQuery.getEngToken(), pageable);
            }
            case KOR_MT -> {
                return bookRepository.findBooksByKorMtFlexible(titleQuery.getKorToken(), pageable);
            }
            case ENG_MT -> {
                return bookRepository.findBooksByEngMtFlexible(titleQuery.getEngToken(), pageable);
            }
            case KOR_ENG -> {
                return bookRepository.findBooksByKorNatural(titleQuery.getEngKorToken(), pageable);
            }
            case ENG_KOR_SG -> {
                return bookRepository.findBooksByEngKorBool(
                    titleQuery.getEngToken(),
                    titleQuery.getKorToken(),
                    pageable);
            }

            case ENG_KOR_MT -> {
                return bookRepository.findBooksByEngKorNatural(
                    titleQuery.getEngToken(),
                    titleQuery.getKorToken(),
                    pageable);
            }
        }

        return null;
    }


    // target에 따라 쿼리 선택하여 동적으로 변동
    private Page<Book> findBooksByTarget(String query, Pageable pageable, String target) {
        if (target.equals("author")) {
            return bookRepository.findBooksByAuthor(query, pageable);
        } else if (target.equals("title")) {
            return bookRepository.findBooksByKorMtFlexible(query, pageable);
        } else {
            return null; //api 추가될 것 고려하여 일단 Null로 넣어놓음
        }
    }

}





