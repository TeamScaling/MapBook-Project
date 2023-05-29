package com.scaling.libraryservice.commons.caching;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class CustomCacheManagerTest {

    private CustomCacheManager<BookSearchService, BookCacheKey, RespBooksDto> cacheManager;

    @Mock
    private BookSearchService bookSearchService;

    private CacheKey<BookCacheKey, RespBooksDto> bookCacheKey;

    private RespBooksDto respBooksDto;

    private Cache<CacheKey<BookCacheKey,RespBooksDto>, RespBooksDto> cache;

    @BeforeEach
    public void setUp() {
        cacheManager = new CustomCacheManager<>();
        bookCacheKey = new BookCacheKey("자바", 1);
        respBooksDto = new RespBooksDto(new MetaDto(),new ArrayList<>());
        cache = Caffeine.newBuilder().build();
    }


    @DisplayName("<Learning Test>캐쉬 데이터가 사용되면 유효기간이 갱신 된다.")
    public void load() throws InterruptedException {
        /* given */

        Cache<Integer, String> bookCache = Caffeine.newBuilder()
            .expireAfterAccess(5, TimeUnit.SECONDS)
            .maximumSize(1000)
            .build();
        /* when */

        bookCache.put(1, "1번");
        bookCache.put(2, "2번");
        /* then */

        Thread.sleep(1000 * 4);
        assertNotNull(bookCache.getIfPresent(1));
        Thread.sleep(1000 * 5);
        assertNull(bookCache.getIfPresent(1));
        assertNull(bookCache.getIfPresent(2));
    }

    @DisplayName("<Learning Test> 캐싱 서버를 염두한 Learning Test Code")
    public void cache_server() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonCacheData = new ObjectMapper().writeValueAsString("안녕하세요");

        // 백업 API에 요청을 보냅니다.
        HttpEntity<String> request = new HttpEntity<>(jsonCacheData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8086/test",
            request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Cache data for [{}] backed up successfully");
        } else {
            System.out.println("Failed to back up cache data for [{}]");
        }
    }

    @Test
    void onShutdown() {
        /* given */

        /* when */

        /* then */
    }

    @Test
    void reloadCacheData() {
        /* given */

        /* when */

        /* then */
    }

    @Test
    void registerCaching() {
        /* given */

        /* when */

        Executable e = () -> cacheManager.registerCaching(cache, bookSearchService);

        /* then */

        assertDoesNotThrow(e);
    }


    @Test
    void put() {

        CacheKey<BookCacheKey, RespBooksDto> bookCacheKey = new BookCacheKey("자바", 1);
    }

    @Test
    void get() {
        /* given */
        cacheManager.registerCaching(cache,bookSearchService);
        cacheManager.put(bookSearchService,bookCacheKey,respBooksDto);

        /* when */

        var result = cacheManager.get(bookSearchService,bookCacheKey);

        /* then */
        assertEquals(result,respBooksDto);
    }

    @Test @DisplayName("다른 캐싱키로 각기 다른 데이터를 구분 할 수 있다.")
    void distinguish_cacheKey() {
        /* given */
        cacheManager.registerCaching(cache,bookSearchService);
        cacheManager.put(bookSearchService,bookCacheKey,respBooksDto);

        BookCacheKey bookCacheKey1 = new BookCacheKey("스프링",1);
        RespBooksDto respBooksDto1 = new RespBooksDto(new MetaDto(),new ArrayList<>());

        cacheManager.put(bookSearchService,bookCacheKey1,respBooksDto1);

        /* when */

        var result = cacheManager.get(bookSearchService,bookCacheKey);
        var result1 = cacheManager.get(bookSearchService,bookCacheKey1);

        /* then */
        assertNotEquals(result,result1);
    }

    @Test
    void removeCaching_success() {
        /* given */
        cacheManager.registerCaching(cache, bookSearchService);

        /* when */
        cacheManager.removeCaching(bookSearchService);
        var result = cacheManager.isUsingCaching(bookSearchService);

        /* then */

        assertFalse(result);
    }

    @Test
    void removeCaching_fail_throwError() {
        /* given */

        /* when */
        Executable e = () -> cacheManager.removeCaching(bookSearchService);

        /* then */

        assertThrows(IllegalArgumentException.class,e);
    }

    @Test
    void isUsingCaching() {
        /* given */

        cacheManager.registerCaching(cache, bookSearchService);
        /* when */

        var result = cacheManager.isUsingCaching(bookSearchService);

        /* then */

        assertTrue(result);
    }

    @Test
    void isContainItem_true() {
        /* given */

        cacheManager.registerCaching(cache, bookSearchService);
        cacheManager.put(bookSearchService,bookCacheKey,respBooksDto);
        /* when */

        var result = cacheManager.isContainItem(bookSearchService,bookCacheKey);

        /* then */

        assertTrue(result);
    }

    @Test
    void isContainItem_false() {
        /* given */

        cacheManager.registerCaching(cache, bookSearchService);

        /* when */

        var result = cacheManager.isContainItem(bookSearchService,bookCacheKey);

        /* then */

        assertFalse(result);
    }

    @Test @DisplayName("캐싱에 등록되지 않은 상태에서 캐싱 데이터를 찾으려고 할 때 에러를 던진다")
    void isContainItem_error() {
        /* given */

        /* when */

        Executable e = () -> cacheManager.isContainItem(bookSearchService,bookCacheKey);

        /* then */

        assertThrows(IllegalArgumentException.class,e);
    }

    @Test @DisplayName("제공 받은 매개 변수 배열에서 CacheKey 타입을 찾아서 반환 할 수 있다.")
    void generateCacheKey_super_success() {
        /* given */
        CacheKey<BookCacheKey, RespBooksDto> bookCacheKey = new BookCacheKey("자바", 1);
        String query = "제목";
        int page = 1;

        Object[] args = new Object[]{query,page,bookCacheKey};

        /* when */

        Executable e = () -> cacheManager.generateCacheKey(args);

        /* then */

        assertDoesNotThrow(e);
    }

    @Test @DisplayName("상위 타입 CacheKey 뿐만 아니라 하위 타입 매개 변수로도 반환 가능 하다.")
    void generateCacheKey_sub_success() {
        /* given */
        BookCacheKey bookCacheKey = new BookCacheKey("자바", 1);
        String query = "제목";
        int page = 1;

        Object[] args = new Object[]{query,page,bookCacheKey};

        /* when */

        Executable e = () -> cacheManager.generateCacheKey(args);

        /* then */

        assertDoesNotThrow(e);
    }

    @Test @DisplayName("제공 받은 매개 변수 배열에서 CacheKey 타입을 찾아서 반환 할 수 있어 에러를 던진다.")
    void generateCacheKey_fail() {
        /* given */
        String query = "제목";
        int page = 1;

        Object[] args = new Object[]{query,page};

        /* when */

        Executable e = () -> cacheManager.generateCacheKey(args);

        /* then */

        assertThrows(UnsupportedOperationException.class,e);
    }

}