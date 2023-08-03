package com.scaling.libraryservice.commons.async;

import com.scaling.libraryservice.commons.caching.CustomCacheManager;
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
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * SearchAsyncExecutor 클래스는 AsyncExecutor 인터페이스를 구현하며, 도서 검색을 비동기적으로 수행합니다. 이 클래스는 검색
 * 요청(ReqBookDto)을 받아 도서 검색 결과인 Page<BookDto>를 반환합니다.
 *
 * @param <T> 실행 결과의 타입. 이 클래스에서는 Page<BookDto>.
 * @param <V> 실행에 필요한 값의 타입. 이 클래스에서는 ReqBookDto.
 */
@RequiredArgsConstructor

@Component
public class SearchAsyncExecutor<T, V> implements AsyncExecutor<Page<BookDto>, ReqBookDto> {

    /**
     * 사용자의 검색 요청과 그에 따른 검색 결과를 캐싱하는 CustomCacheManager입니다.
     */
    private final CustomCacheManager<ReqBookDto, RespBooksDto> cacheManager;


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

        Page<BookDto> booksPage = Page.empty();

        try {
            booksPage = isAsync ? executeAsync(supplier, reqBookDto, timeout) : supplier.get();
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            asyncSearchBook(supplier, reqBookDto);
        }
        return booksPage;
    }

    private Page<BookDto> executeAsync(Supplier<Page<BookDto>> supplier, ReqBookDto reqBookDto,
        int timeout)
        throws ExecutionException, InterruptedException, TimeoutException {

        return CompletableFuture.supplyAsync(supplier).get(timeout, TimeUnit.SECONDS);
    }

    /**
     * 비동기적으로 도서를 검색하고, 결과를 캐시에 저장합니다.
     *
     * @param supplier   도서 검색 작업을 제공하는 Supplier
     * @param reqBookDto 검색 요청 정보
     */
    void asyncSearchBook(Supplier<Page<BookDto>> supplier, @NonNull ReqBookDto reqBookDto) {
        CompletableFuture.runAsync(
            () -> {
                Page<BookDto> fetchedBooks = supplier.get();
                cachingAsyncResult(fetchedBooks, reqBookDto);
            });
    }

    private void cachingAsyncResult(Page<BookDto> fetchedBooks, ReqBookDto reqBookDto) {
        RespBooksDto respBooksDto = RespBooksDtoFactory.createDefaultRespBooksDto(
            fetchedBooks,
            reqBookDto
        );

        cacheManager.put(BookSearchService.class, reqBookDto, respBooksDto);
    }
}
