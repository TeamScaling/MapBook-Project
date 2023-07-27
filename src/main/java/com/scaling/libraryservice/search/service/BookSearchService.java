package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.commons.async.AsyncExecutor;
import com.scaling.libraryservice.commons.caching.CustomCacheable;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.util.TitleAnalyzer;
import com.scaling.libraryservice.search.util.TitleQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

/**
 * 도서 검색 기능을 제공하는 서비스 클래스입니다. 입력된 검색어에 따라 적절한 검색 쿼리를 선택하여 도서를 검색하고, 결과를 반환합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Timer
public class BookSearchService {

    private final TitleAnalyzer titleAnalyzer;
    private final BookRepository bookRepository;
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
    public RespBooksDto searchBooks(@NonNull ReqBookDto reqBookDto, int timeout, boolean isAsync)
        throws IllegalArgumentException {

        String query = reqBookDto.getQuery();

        log.info("-------------query : [{}]-------------------------------", query);

        Pageable pageable
            = PageRequest.of(reqBookDto.getPage() - 1, reqBookDto.getSize());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        TitleQuery titleQuery = titleAnalyzer.analyze(query,true);

        if (titleQuery.isEmptyTitleQuery()) {
            return RespBooksDto.emptyDto();
        }

        Page<BookDto> booksPage = asyncExecutor.execute(
            () -> bookRepository.findBooks(titleQuery, pageable), reqBookDto, timeout, isAsync);

        stopWatch.stop();

        String searchTime = String.format("%.3f", stopWatch.getTotalTimeSeconds());

        return new RespBooksDto(
            new MetaDto(booksPage, reqBookDto, searchTime, titleQuery.getOriginalQuery())
            , booksPage);
    }

    public RespBooksDto bookAutoComplete(ReqBookDto reqBookDto, int timeout) {

        RespBooksDto respBooksDto = searchBooks(reqBookDto, timeout, false);

        if(respBooksDto.getDocuments().size() > 0){
            //맨위에 결과값이 front에서 짤려 보이는 문제 해결하기 위해 빈 제목을 넣는다.
            List<BookDto> books = respBooksDto.getDocuments();
            books.add(0, BookDto.emptyDto());
        }

        return respBooksDto;
    }

}





