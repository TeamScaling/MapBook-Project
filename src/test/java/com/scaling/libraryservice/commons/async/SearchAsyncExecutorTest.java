package com.scaling.libraryservice.commons.async;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.commons.caching.MapBookCacheManager;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.repository.BookRepoQueryDsl;
import com.scaling.libraryservice.search.service.BookSearchService;
import com.scaling.libraryservice.search.engine.TitleQuery;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class SearchAsyncExecutorTest {

    @InjectMocks
    private SearchAsyncExecutor executor;

    @Mock
    private BookRepoQueryDsl bookRepoQueryDsl;

    @Mock
    private TitleQuery titleQuery;

    @Mock
    private ReqBookDto reqBookDto;

    @Mock
    private Page<BookDto> page;

    @Mock
    MapBookCacheManager<ReqBookDto, RespBooksDto> mockCacheManager;


    @Test
    @DisplayName("TimeOut 미만의 작업을 비동기를 사용하지 않고도 반환 한다.")
    void execute() {
        /* given */
        Pageable pageable = PageRequest.of(1, 10);

        when(bookRepoQueryDsl.findBooks(titleQuery, pageable))
            .thenReturn(page);

        /* when */

        var result = executor.execute(() -> bookRepoQueryDsl.findBooks(titleQuery, pageable),
            reqBookDto, 3, true);

        /* then */

        assertNotNull(result);
    }

    @Test
    @DisplayName("정해진 타임아웃이 지나면 빈결과를 반환하고, 비동기 작업에 들어 간다.")
    public void execute2() {
        /* given */
        Pageable pageable = PageRequest.of(1, 10);

        when(bookRepoQueryDsl.findBooks(titleQuery, pageable))
            .thenAnswer(invocation -> {
                Thread.sleep(1100); // 1.1 seconds delay
                return page;
            });

        /* when */

        Page<BookDto> result = executor.execute(() ->
                bookRepoQueryDsl.findBooks(titleQuery, pageable),
            reqBookDto,
            1,
            true);

        /* then */
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("비동기 작업 완료 결과를 캐싱 처리 한다.")
    public void execute3() throws InterruptedException {
        /* given */
        Pageable pageable = PageRequest.of(1, 10);

        when(bookRepoQueryDsl.findBooks(titleQuery, pageable))
            .thenAnswer(invocation -> {
                Thread.sleep(1100);
                return page;
            });

        Supplier<Page<BookDto>> supplier = () -> bookRepoQueryDsl.findBooks(titleQuery, pageable);

        /* when */

        executor.execute(supplier, reqBookDto,1,true);

        // Caching 처리 될 때까지 기다린다.
        Thread.sleep(1000);

        /* then */
        verify(mockCacheManager).put(eq(BookSearchService.class), eq(reqBookDto),
            any(RespBooksDto.class));

    }
}