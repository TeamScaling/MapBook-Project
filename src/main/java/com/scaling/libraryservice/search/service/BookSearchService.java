package com.scaling.libraryservice.search.service;

import static com.scaling.libraryservice.search.dto.RespBooksDtoFactory.createDefaultRespBooksDto;
import static com.scaling.libraryservice.search.dto.RespBooksDtoFactory.createEmptyRespBookDto;
import static com.scaling.libraryservice.search.dto.RespBooksDtoFactory.createIsbnRespBookDto;
import static com.scaling.libraryservice.search.dto.RespBooksDtoFactory.createOneBookRespDto;

import com.scaling.libraryservice.commons.async.AsyncExecutor;
import com.scaling.libraryservice.commons.caching.aop.CustomCacheable;
import com.scaling.libraryservice.commons.timer.MeasureTaskTime;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.engine.TitleAnalyzer;
import com.scaling.libraryservice.search.engine.TitleQuery;
import com.scaling.libraryservice.search.engine.util.SubTitleRemover;
import com.scaling.libraryservice.search.exception.NotQualifiedQueryException;
import com.scaling.libraryservice.search.repository.BookRepoQueryDsl;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * 도서 검색 기능을 제공하는 서비스 클래스입니다. 입력된 검색어에 따라 적절한 검색 쿼리를 선택하여 도서를 검색하고, 결과를 반환합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@MeasureTaskTime
public class BookSearchService {

    private final TitleAnalyzer titleAnalyzer;
    private final BookRepoQueryDsl bookRepoQueryDsl;
    private final AsyncExecutor<Page<BookDto>, ReqBookDto> asyncExecutor;

    private final static String ISBN_REGEX = "\\d+";
    private final static int ISBN_MIN_SIZE = 10;

    private final static int MATCHING_LIMIT_PAGE = 1;

    /**
     * 검색어를 이용하여 도서를 검색하고 그 결과를 반환하는 메서드입니다. 이 메서드는 페이지당 도서 수와 검색 대상을 파라미터로 받습니다. 만약 검색이 3초 이상 소요될
     * 경우, 비동기적으로 검색을 수행하고 빈 결과를 즉시 반환합니다.
     *
     * @param reqBookDto 검색어와 페이지 번호 그리고 페이지당 반환할 도서 수을 담는 요청 객체.
     * @param timeout    비동기 검색 기능에 대한 시간 설정.
     * @return 검색 결과를 담은 RespBooksDto 객체. 만약 검색이 3초를 초과하면 빈 결과가 반환됩니다.
     */
    @CustomCacheable
    @MeasureTaskTime
    public RespBooksDto searchBooks(@NonNull ReqBookDto reqBookDto, int timeout,
        boolean isAsyncSupport)
        throws NotQualifiedQueryException {

        String userQuery = reqBookDto.getUserQuery();

        if (isIsbnQuery(userQuery)) {
            return searchBookByIsbn(userQuery);
        }

        TitleQuery titleQuery = titleAnalyzer.analyze(userQuery, true);

        return titleQuery.isEmptyTitleQuery() ?
            createEmptyRespBookDto(reqBookDto.getUserQuery())
            : searchBookWithAsync(titleQuery, reqBookDto, timeout, isAsyncSupport);
    }

    private RespBooksDto searchBookByIsbn(String userQuery) {

        BookDto bookDto = bookRepoQueryDsl.findBooksByIsbn(userQuery);

        return bookDto.isEmpty() ?
            createEmptyRespBookDto(userQuery)
            : createIsbnRespBookDto(bookDto, userQuery);
    }


    private RespBooksDto searchBookWithAsync(TitleQuery titleQuery, ReqBookDto reqBookDto,
        int timeout, boolean isAsyncSupport) {

        Page<BookDto> books = bookRepoQueryDsl.findBooks(titleQuery,createPageableFromRequest(reqBookDto));
//            asyncExecutor.execute(
//                createFindBooksTask(titleQuery, createPageableFromRequest(reqBookDto))
//                , reqBookDto
//                , timeout
//                , isAsyncSupport
//            );

        // 검색 결과와 사용자 검색어가 일치하면 일치하는 도서만 반환 한다.
        Optional<BookDto> potentialMatchBook = matchingQueryAndTitle(books, reqBookDto);

        return potentialMatchBook
            .map(bookDto -> createOneBookRespDto(reqBookDto.getUserQuery(), bookDto))
            .orElseGet(() -> createDefaultRespBooksDto(books, reqBookDto));
    }

    private Optional<BookDto> matchingQueryAndTitle(@NonNull Page<BookDto> booksPage,
        ReqBookDto reqBookDto) {

        return booksPage.stream()
            .filter(bookDto ->
                isUserQueryMatchingBook(
                    reqBookDto.getUserQuery(),
                    bookDto,
                    reqBookDto.getPage()
                )
            )
            .findAny();
    }

    private boolean isUserQueryMatchingBook(String userQuery, @NonNull BookDto bookDto, int page) {

        String mainTitle = SubTitleRemover.removeSubTitle(bookDto.getTitle()).trim();
        return mainTitle.equals(userQuery.trim()) && page == MATCHING_LIMIT_PAGE;
    }

    private Supplier<Page<BookDto>> createFindBooksTask(TitleQuery titleQuery, Pageable pageable) {
        return () -> bookRepoQueryDsl.findBooks(titleQuery, pageable);
    }

    private Pageable createPageableFromRequest(ReqBookDto reqBookDto) {
        return PageRequest.of(reqBookDto.getPage() - 1, reqBookDto.getSize());
    }

    boolean isIsbnQuery(String isbn) {
        return isbn.length() >= ISBN_MIN_SIZE && isbn.matches(ISBN_REGEX);
    }

    @MeasureTaskTime
    public RespBooksDto autoCompleteSearch(ReqBookDto reqBookDto, int timeout,
        boolean isAsyncSupport) {

        return searchBooks(reqBookDto, timeout, isAsyncSupport);
    }

}





