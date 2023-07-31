package com.scaling.libraryservice.search.service;

import static com.scaling.libraryservice.search.dto.RespBooksDto.emptyRespBookDto;
import static com.scaling.libraryservice.search.dto.RespBooksDto.isbnRespBookDto;

import com.scaling.libraryservice.commons.async.AsyncExecutor;
import com.scaling.libraryservice.commons.caching.CustomCacheable;
import com.scaling.libraryservice.commons.timer.MeasureTaskTime;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.exception.NotQualifiedQueryException;
import com.scaling.libraryservice.search.repository.BookRepoQueryDsl;
import com.scaling.libraryservice.search.engine.TitleAnalyzer;
import com.scaling.libraryservice.search.engine.TitleQuery;
import java.util.List;
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
    private final BookRepoQueryDsl bookRepository;
    private final AsyncExecutor<Page<BookDto>, ReqBookDto> asyncExecutor;

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
        boolean isAsyncSupport) throws NotQualifiedQueryException {

        String userQuery = reqBookDto.getQuery();

        if (isIsbnQuery(userQuery)) {
            return searchBookByIsbn(userQuery);
        }

        TitleQuery titleQuery = titleAnalyzer.analyze(userQuery, true);

        return titleQuery.isEmptyTitleQuery() ?
            emptyRespBookDto(userQuery)
            : searchBookWithAsync(titleQuery, reqBookDto, timeout, isAsyncSupport);
    }

    private RespBooksDto searchBookByIsbn(String userQuery) {

        BookDto bookDto = bookRepository.findBooksByIsbn(userQuery);

        return bookDto.isEmpty() ? emptyRespBookDto(userQuery)
            : isbnRespBookDto(userQuery, bookDto);
    }

    boolean isIsbnQuery(String isbn) {
        return isbn.length() >= 10 && isbn.matches("\\d+");
    }


    private RespBooksDto searchBookWithAsync(TitleQuery titleQuery, ReqBookDto reqBookDto,
        int timeout, boolean isAsyncSupport) {

        Page<BookDto> booksPage =
            asyncExecutor.execute(
                createFindBooksTask(titleQuery, createPageableFromRequest(reqBookDto))
                , reqBookDto, timeout, isAsyncSupport);

        return new RespBooksDto(new MetaDto(booksPage, reqBookDto), booksPage);
    }

    private Supplier<Page<BookDto>> createFindBooksTask(TitleQuery titleQuery, Pageable pageable) {
        return () -> bookRepository.findBooks(titleQuery, pageable);
    }

    private Pageable createPageableFromRequest(ReqBookDto reqBookDto) {
        return PageRequest.of(reqBookDto.getPage() - 1, reqBookDto.getSize());
    }

    public List<BookDto> autoCompleteSearch(ReqBookDto reqBookDto, int timeout,
        boolean isAsyncSupport) {

        return searchBooks(reqBookDto, timeout, isAsyncSupport).getDocuments();
    }

}





