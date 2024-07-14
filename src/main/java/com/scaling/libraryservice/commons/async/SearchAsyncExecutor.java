package com.scaling.libraryservice.commons.async;

import com.scaling.libraryservice.commons.caching.MapBookCacheManager;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.RespBooksDtoFactory;
import com.scaling.libraryservice.search.service.BookSearchService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * SearchAsyncExecutor 클래스는 AsyncExecutor 인터페이스를 구현하며, 도서 검색을 비동기적으로 수행합니다. 이 클래스는 검색
 * 요청(ReqBookDto)을 받아 도서 검색 결과인 Page<BookDto>를 반환합니다.
 */
@RequiredArgsConstructor
@Component
public class SearchAsyncExecutor implements AsyncExecutor<Page<BookDto>, ReqBookDto> {

    /**
     * 사용자의 검색 요청과 그에 따른 검색 결과를 캐싱하는 CustomCacheManager입니다.
     */
    private final MapBookCacheManager<ReqBookDto, RespBooksDto> cacheManager;

    /**
     * 비동기적으로 도서 검색을 수행하고, 결과를 반환합니다. 검색 작업이 지정된 시간을 초과하면 TimeoutException을 발생시키며, 이 경우 빈 결과를 반환하고
     * 비동기적으로 검색을 계속 수행합니다.
     *
     * @param supplier   도서 검색 작업을 제공하는 Supplier
     * @param reqBookDto 검색 요청 정보
     * @param timeout    작업의 최대 실행 시간(단위: 초)
     * @return 도서 검색 결과를 담은 Page<BookDto> 객체
     */
    @Override
    public Page<BookDto> execute(Supplier<Page<BookDto>> supplier, ReqBookDto reqBookDto,
        int timeout, boolean isAsync) {
        return isAsync ? executeAsync(supplier, reqBookDto, timeout)
            : supplier.get();
    }

    /**
     * 비동기적으로 도서를 검색하고, 타임 아웃이 되면 일단 빈 결과를 반환한 뒤 비동기 검색 쓰레드의 완료 시점에 결과를 캐시에 저장합니다.
     *
     * @param supplier   도서 검색 작업을 제공하는 Supplier
     * @param reqBookDto 검색 요청 정보
     */
    private Page<BookDto> executeAsync(Supplier<Page<BookDto>> supplier, ReqBookDto reqBookDto,
        int timeout) {
        CompletableFuture<Page<BookDto>> future = CompletableFuture.supplyAsync(supplier);
        try {
            return future.get(timeout, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            future.thenAccept(result -> cachingAsyncResult(result, reqBookDto));
            return Page.empty();
        }
    }

    // cache Manager를 호출해 비동기 결과를 캐싱 처리
    private void cachingAsyncResult(Page<BookDto> fetchedBooks, ReqBookDto reqBookDto) {
        RespBooksDto respBooksDto = RespBooksDtoFactory.createDefaultRespBooksDto(fetchedBooks,
            reqBookDto);
        cacheManager.put(BookSearchService.class, reqBookDto, respBooksDto);
    }
}
