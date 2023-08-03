package com.scaling.libraryservice.commons.caching;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.commons.caching.aop.CustomCacheAspect;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.service.MapBookService;
import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StopWatch;

@ExtendWith(MockitoExtension.class)
class CustomCacheAspectTest {

    @InjectMocks
    private CustomCacheAspect<ReqBookDto, RespBooksDto> cacheAspect;

    @InjectMocks
    private CustomCacheAspect<ReqMapBookDto, RespMapBookDto> cacheAspect2;

    @Mock
    private CustomCacheManager<ReqBookDto, RespBooksDto> cacheManager;

    @Mock
    private CustomCacheManager<ReqMapBookDto, RespMapBookDto> cacheManager2;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private BookSearchService bookSearchService;

    @Mock
    private MapBookService mapBookService;

    @Mock
    private StopWatch stopWatch;
    @Mock
    private CacheKey<ReqBookDto, RespBooksDto> bookCacheKey;
    @Mock
    private RespBooksDto respBooksDto;

    @Mock
    private CacheKey<ReqMapBookDto,RespMapBookDto> mapBookCacheKey;


    @BeforeEach
    public void setUp(){
        cacheAspect = new CustomCacheAspect<>(cacheManager);
        cacheAspect2 = new CustomCacheAspect<>(cacheManager2);
    }

    @Test @DisplayName("캐싱 되지 않은 상태")
    void cacheAround() throws Throwable {
        /* given */

        when(joinPoint.getTarget()).thenReturn(bookSearchService);
        when(cacheManager.generateCacheKey(any())).thenReturn(bookCacheKey);
        when(cacheManager.isUsingCaching(bookSearchService.getClass())).thenReturn(false);
        when(joinPoint.proceed()).thenReturn(respBooksDto);

        /* when */
        var result = cacheAspect.cacheAround(joinPoint);

        /* then */

        assertEquals(result,respBooksDto);
    }

    @Test @DisplayName("MapBookService 관련 데이터 캐싱 처리")
    void patchCacheManager_mapBook() throws Throwable {
        /* given */
        RespMapBookDto respMapBookDto = mock(RespMapBookDto.class);
        when(joinPoint.proceed()).thenReturn(respMapBookDto);


        /* when */

        var result = cacheAspect2.patchCacheManager(joinPoint, mapBookService.getClass(),mapBookCacheKey);

        /* then */

        verify(cacheManager2,times(1)).put(any(),any(),any());
    }

    @Test @DisplayName("일정 시간 초과 요청에 대한 캐싱 처리")
    void patchCacheManager_overTime() throws Throwable {
        /* given */
        when(joinPoint.proceed()).thenReturn(respBooksDto);
        /* when */

        cacheAspect.patchCacheManager(joinPoint, MapBookService.class,bookCacheKey);

        /* then */
        verify(cacheManager,times(1)).put(any(),any(),any());
    }
    @Test @DisplayName("해당 target이 캐싱 서비스가 등록 되지 않았을 때 캐싱 서비스 등록이 정상")
    void cacheAround_registerCaching() throws Throwable {
        /* given */

        when(joinPoint.getTarget()).thenReturn(bookSearchService);
        when(cacheManager.generateCacheKey(any())).thenReturn(bookCacheKey);
        when(cacheManager.isUsingCaching(bookSearchService.getClass())).thenReturn(false);

        /* when */
        var result = cacheAspect.cacheAround(joinPoint);

        /* then */

        verify(cacheManager,times(1)).registerCaching(any(),any());
    }

    @Test @DisplayName("캐싱 된 데이터를 찾아서 반환 해 줄 때")
    void cacheAround2() throws Throwable {
        /* given */

        when(joinPoint.getTarget()).thenReturn(bookSearchService);
        when(cacheManager.generateCacheKey(any())).thenReturn(bookCacheKey);
        when(cacheManager.isUsingCaching(bookSearchService.getClass())).thenReturn(true);
        when(cacheManager.isContainItem(any(), any())).thenReturn(true);

        /* when */
        var result = cacheAspect.cacheAround(joinPoint);

        /* then */

        verify(cacheManager,times(1)).get(any(),any());
    }



}