package com.scaling.libraryservice.search.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import com.scaling.libraryservice.commons.caching.Customer;
import com.scaling.libraryservice.commons.caching.NameCacheKey;
import com.scaling.libraryservice.commons.caching.UserInfo;
import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomCacheManagerTest {

    private CustomCacheManager customCacheManager;
    @Autowired
    private BookSearchService bookSearchService;

    private BookCacheKey bookCacheKey;

    @BeforeEach
    public void setUp(){
        customCacheManager = new CustomCacheManager<>(null);
        bookCacheKey = new BookCacheKey("자바",1);
    }


//    @PostConstruct
//    public void init(){
//
//
//
//        Cache<CacheKey,Object> cache = Caffeine.newBuilder()
//            .expireAfterWrite(600, TimeUnit.SECONDS)
//            .maximumSize(1000).build();
//
//        customCacheManager.registerCaching(cache,bookSearchService.getClass());
//
//        bookCacheKey = new BookCacheKey("자바",1);
//
//    }

    @Test
    public void get_value_of_the_cacheKey (){
        /* given */
        Customer customer = new Customer("이름 서비스");

        Cache<CacheKey<NameCacheKey,UserInfo>,UserInfo> cache = Caffeine.newBuilder()
            .expireAfterWrite(600, TimeUnit.SECONDS)
            .maximumSize(1000).build();

        customCacheManager.registerCaching(cache, customer);

        NameCacheKey key = new NameCacheKey("조인준",34);
        NameCacheKey key2 = new NameCacheKey("홍길동",30);

        UserInfo userInfo1 = new UserInfo("조인준",34,"1234");
        UserInfo userInfo2 = new UserInfo("홍길동",30,"1111");

        customCacheManager.put(customer,key,userInfo1);
        customCacheManager.put(customer,key2,userInfo2);

        /* when */

        var result = customCacheManager.get(customer,key);
        var result2 = customCacheManager.get(customer,key2);

        /* then */

        assertEquals(userInfo1,result);
        assertEquals(userInfo2,result2);
    }

    @Test
    public void get_value_of_the_cacheKey2(){
        /* given */
        Customer customer = new Customer("한글 서비스");
        Customer customer2 = new Customer("영어 서비스");

        Cache<CacheKey<NameCacheKey,UserInfo>,UserInfo> cache = Caffeine.newBuilder()
            .expireAfterWrite(600, TimeUnit.SECONDS)
            .maximumSize(1000).build();

        customCacheManager.registerCaching(cache, customer);
        customCacheManager.registerCaching(cache, customer2);

        NameCacheKey key1 = new NameCacheKey("조인준",34);
        NameCacheKey key2 = new NameCacheKey("joinjun",34);

        UserInfo userInfo1 = new UserInfo("조인준",34,"1234");
        UserInfo userInfo2 = new UserInfo("joinjun",34,"1234");

        customCacheManager.put(customer,key1,userInfo1);
        customCacheManager.put(customer2,key2,userInfo2);



        /* when */

        var result1 = customCacheManager.get(customer,key1);
        var result2 = customCacheManager.get(customer2,key2);

        /* then */

        System.out.println(result1);
        System.out.println(result2);

        assertEquals(userInfo1,result1);
        assertEquals(userInfo2,result2);
    }

    @Test @DisplayName("캐싱 기능이 잘 작동 하는지 확인")
    public void searchService_cache(){
        /* given */

        /* when */

        BookCacheKey bookCacheKey = new BookCacheKey("자바",1);

        var booksDto = bookSearchService.searchBooks(bookCacheKey,10,"title");

        customCacheManager.put(bookSearchService.getClass(), bookCacheKey,booksDto);

        /* then */


        var result= (RespBooksDto) customCacheManager.get(bookSearchService,
            bookCacheKey);

        assertEquals(booksDto,result);
    }

    @Test @DisplayName("캐싱 기능 해제")
    public void cache_remove_caching(){
        /* given */
        Cache<BookCacheKey,RespBooksDto> cache = bookCacheKey.configureCache();

        customCacheManager.registerCaching(cache,bookSearchService);

            /* when */

        var booksDto = bookSearchService.searchBooks(bookCacheKey,10,"title");

        customCacheManager.put(bookSearchService.getClass(), bookCacheKey,booksDto);

        /* then */
        var result= (RespBooksDto) customCacheManager.get(bookSearchService.getClass(),
            bookCacheKey);

        assertEquals(booksDto,result);

        customCacheManager.removeCaching(bookSearchService.getClass());

        var result2= (RespBooksDto) customCacheManager.get(bookSearchService.getClass(),
            bookCacheKey);

        assertNull(result2);
    }

    @Test @DisplayName("캐싱 기능 해제")
    public void cache_terminate_caching(){
        /* given */

        var booksDto = bookSearchService.searchBooks(bookCacheKey,10,"title");

        customCacheManager.put(bookSearchService.getClass(), bookCacheKey,booksDto);

        /* when */
        var result= (RespBooksDto) customCacheManager.get(bookSearchService.getClass(),
            bookCacheKey);

        assertNotNull(result);
        customCacheManager.removeCaching(bookSearchService.getClass());
        var result2= (RespBooksDto) customCacheManager.get(bookSearchService.getClass(),
            bookCacheKey);
        /* then */
        assertNull(result2);
    }

    @Test @DisplayName("캐싱 기능 해제")
    public void cache_is_UsingCaching(){
        /* given */

        var booksDto = bookSearchService.searchBooks(bookCacheKey,10,"title");

        customCacheManager.put(bookSearchService.getClass(), bookCacheKey,booksDto);

        /* when */
        var result= (RespBooksDto) customCacheManager.get(bookSearchService.getClass(),
            bookCacheKey);

        boolean result2= customCacheManager.isUsingCaching(bookSearchService.getClass());
        /* then */
        assertFalse(result2);
    }

    @Test
    public void load_file(){
        /* given */

        var booksDto = bookSearchService.searchBooks(bookCacheKey,10,"title");

        Cache<BookCacheKey,RespBooksDto> cache = Caffeine.newBuilder().build();

        cache.put(bookCacheKey,booksDto);

        String filename = "cache_books.ser";

        /* then */

        try {
            FileOutputStream fos = new FileOutputStream(filename);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream os = new ObjectOutputStream(bos);

            os.writeObject(cache);

            os.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}