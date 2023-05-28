package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import com.scaling.libraryservice.commons.caching.CustomCacheable;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import com.scaling.libraryservice.search.domain.TitleQuery;
import com.scaling.libraryservice.search.domain.TitleType;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.util.TitleAnalyzer;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 도서 검색 기능을 제공하는 서비스 클래스입니다. 입력된 검색어에 따라 적절한 검색 쿼리를 선택하여 도서를 검색하고, 결과를 반환합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Timer
@Getter
public class BookSearchService {

    private final BookRepository bookRepository;
    private final CustomCacheManager<BookSearchService,BookCacheKey,RespBooksDto> cacheManager;
    private final TitleAnalyzer titleAnalyzer;

    /**
     * 검색어를 이용하여 도서를 검색하고 그 결과를 반환하는 메서드입니다. 이 메서드는 페이지당 도서 수와 검색 대상을 파라미터로 받습니다.
     * 만약 검색이 3초 이상 소요될 경우, 비동기적으로 검색을 수행하고 빈 결과를 즉시 반환합니다.
     *
     * @param bookCacheKey 검색어와 페이지 번호를 포함하는 캐시 키 객체
     * @param size 페이지당 반환할 도서 수
     * @param target 검색 대상 (기본값: "title")
     * @return 검색 결과를 담은 RespBooksDto 객체. 만약 검색이 3초를 초과하면 빈 결과가 반환됩니다.
     */
    @Timer
    @CustomCacheable
    public RespBooksDto searchBooks(BookCacheKey bookCacheKey,int size,String target) {

        String query = bookCacheKey.getQuery();
        int page = bookCacheKey.getPage();

        log.info("-------------query : [{}]-------------------------------", query);

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Book> books = null;

        TitleQuery titleQuery = titleAnalyzer.analyze(query);

        try {
            books = CompletableFuture.supplyAsync(() -> pickSelectQuery(titleQuery, pageable))
                .get(3, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Query execution exceeded 3 seconds. Returning an empty result.", e);
            books = Page.empty(pageable);
            asyncSearchBook(query, page, size,titleQuery);
        }

        Objects.requireNonNull(books);

        return new RespBooksDto(
            new MetaDto(books.getTotalPages(),
                books.getTotalElements(), page, size),
            books.stream().map(BookDto::new).toList());
    }

    public void asyncSearchBook(String query, int page, int size,TitleQuery titleQuery) {

        Pageable pageable = PageRequest.of(page - 1, size);
        log.info("[{}] async Search Book start.....", query);

        var result = CompletableFuture.runAsync(() -> {
            Page<Book> fetchedBooks = pickSelectQuery(titleQuery, pageable);

            if (fetchedBooks != null && !fetchedBooks.isEmpty()) {
                RespBooksDto respBooksDto = new RespBooksDto(
                    new MetaDto(fetchedBooks.getTotalPages(),
                        fetchedBooks.getTotalElements(), page, size),
                    fetchedBooks.stream().map(BookDto::new).toList());

                cacheManager.put(this, new BookCacheKey(query, page), respBooksDto);
                log.info("[{}] async Search task is Completed", query);
            }
        });
    }


    /**
     * 검색 대상(target)에 따라 적절한 검색 쿼리를 선택하여 도서를 검색하고, 결과를 반환하는 메서드입니다.
     *
     * @param titleQuery  검색 쿼리를 분석한 결과를 담고 있는 TitleQuery 객체
     * @param pageable 페이지 관련 설정을 담은 Pageable 객체
     * @return 검색 결과를 담은 Page<Book> 객체
     */
    public Page<Book> pickSelectQuery(TitleQuery titleQuery, Pageable pageable) {

        TitleType type = titleQuery.getTitleType();

        switch (type) {

            case KOR_SG, KOR_MT_OVER_TWO -> {
                return bookRepository.findBooksByKorNatural(titleQuery.getKorToken(), pageable);
            }
            case ENG_SG -> {
                return bookRepository.findBooksByEngBool(titleQuery.getEngToken(), pageable);
            }
            case KOR_MT_UNDER_TWO -> {
                return bookRepository.findBooksByKorMtFlexible(titleQuery.getKorToken(), pageable);
            }

            case ENG_MT -> {
                return bookRepository.findBooksByEngMtFlexible(titleQuery.getEngToken(), pageable);
            }

            case KOR_ENG, ENG_KOR_SG -> {
                return bookRepository.findBooksByEngKorBool(titleQuery.getEngToken(),
                    titleQuery.getKorToken(), pageable);
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

}





