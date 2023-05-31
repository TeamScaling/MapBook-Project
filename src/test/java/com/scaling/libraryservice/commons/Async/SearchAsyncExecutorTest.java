package com.scaling.libraryservice.commons.Async;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import com.scaling.libraryservice.commons.reporter.SlowTaskReporter;
import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookFinder;
import com.scaling.libraryservice.search.service.BookSearchService;
import com.scaling.libraryservice.search.util.TitleQuery;
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
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SearchAsyncExecutorTest {

    @InjectMocks
    private SearchAsyncExecutor executor;

    @Mock
    private BookFinder bookFinder;

    @Mock
    private TitleQuery titleQuery;

    @Mock
    private ReqBookDto reqBookDto;

    @Test
    @DisplayName("3초 미만의 작업을 비동기를 사용하지 않고도 반환 한다.")
    void execute() {
        /* given */
        Pageable pageable = PageRequest.of(1, 10);

        Page<BookDto> pageBook = mock(Page.class);

        when(bookFinder.selectBooks(titleQuery, pageable)).thenReturn(pageBook);

        /* when */

        var result = executor.execute(() -> bookFinder.selectBooks(titleQuery, pageable),
            reqBookDto, 3);

        /* then */

        assertNotNull(result);
    }

    @Test @DisplayName("정해진 타임아웃이 지나면 비동기 작업에 들어 간다.")
    public void execute2() {
        /* given */
        Pageable pageable = PageRequest.of(1, 10);
        Page<BookDto> pageBook = mock(Page.class);

        when(bookFinder.selectBooks(titleQuery, pageable))
            .thenAnswer(invocation -> {
                Thread.sleep(1100); // 1.1 seconds delay
                return pageBook;
            });

        Supplier<Page<BookDto>> supplier;


        /* when */

        var result = executor.execute(() -> bookFinder.selectBooks(titleQuery, pageable),
            reqBookDto, 1);

        /* then */

        assertNotNull(result);
    }

    @Test @DisplayName("비동기 작업 완료 결과를 캐싱 처리 한다.")
    public void execute3() throws InterruptedException {
        /* given */
        Pageable pageable = PageRequest.of(1, 10);
        Page<BookDto> pageBook = mock(Page.class);

        when(bookFinder.selectBooks(titleQuery, pageable))
            .thenAnswer(invocation -> {
                Thread.sleep(1100);
                return pageBook;
            });

        Supplier<Page<BookDto>> supplier = () -> bookFinder.selectBooks(titleQuery, pageable);

        CustomCacheManager<ReqBookDto, RespBooksDto> mockCacheManager = mock(CustomCacheManager.class);
        ReflectionTestUtils.setField(executor, "cacheManager", mockCacheManager);

        /* when */

        executor.asyncSearchBook(supplier,reqBookDto);

        Thread.sleep(1200);

        /* then */
        verify(mockCacheManager).put(eq(BookSearchService.class), eq(reqBookDto), any(RespBooksDto.class));

    }
}