package com.scaling.libraryservice.search.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.caching.CacheBackupService;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import com.scaling.libraryservice.commons.caching.Customer;
import com.scaling.libraryservice.commons.caching.NameCacheKey;
import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomCacheManagerTest {

    @Autowired
    private BookSearchService bookSearchService;

    private CustomCacheManager customCacheManager;

    private BookCacheKey bookCacheKey;

    private ApplicationContext ac;

    @PostConstruct
    public void init(){
        customCacheManager = new CustomCacheManager(new CacheBackupService(ac));

        Cache<CacheKey,Object> cache = Caffeine.newBuilder()
            .expireAfterWrite(600, TimeUnit.SECONDS)
            .maximumSize(1000).build();

        customCacheManager.registerCaching(cache,bookSearchService.getClass());

        bookCacheKey = new BookCacheKey("자바",1);

    }

    @Test @DisplayName("간단한 키를 이용한 기본적인 캐싱 테스트")
    public void load (){
        /* given */


        Customer customer = new Customer("조인준");

        Cache<CacheKey,Object> cache = Caffeine.newBuilder()
            .expireAfterWrite(600, TimeUnit.SECONDS)
            .maximumSize(1000).build();

        customCacheManager.registerCaching(cache, customer.getClass());
        /* when */

        NameCacheKey key = new NameCacheKey("조인준",34);
        NameCacheKey key2 = new NameCacheKey("조인준",30);

        customCacheManager.put(customer.getClass(),key,customer.hello());

        /* then */

        String hello = (String) customCacheManager.get(customer.getClass(),key);
        String hello2 = (String) customCacheManager.get(customer.getClass(),key2);

        System.out.println(hello);
        System.out.println(hello2);
    }

    @Test @DisplayName("캐싱 기능이 잘 작동 하는지 확인")
    public void searchService_cache(){
        /* given */

        /* when */

        BookCacheKey bookCacheKey = new BookCacheKey("자바",1);

        var booksDto = bookSearchService.searchBooks("자바",1,10,"title");

        customCacheManager.put(bookSearchService.getClass(), bookCacheKey,booksDto);

        /* then */


        var result= (RespBooksDto) customCacheManager.get(bookSearchService.getClass(),
            bookCacheKey);

        assertEquals(booksDto,result);
    }

    @Test @DisplayName("캐싱 기능 해제")
    public void cache_remove_caching(){
        /* given */

        Cache<CacheKey,Object> cache = Caffeine.newBuilder()
            .expireAfterWrite(600, TimeUnit.SECONDS)
            .maximumSize(1000).build();

        customCacheManager.registerCaching(cache,bookSearchService.getClass());

        /* when */

        var booksDto = bookSearchService.searchBooks("자바",1,10,"title");

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

        var booksDto = bookSearchService.searchBooks("자바",1,10,"title");

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

        var booksDto = bookSearchService.searchBooks("자바",1,10,"title");

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

        BookCacheKey bookCacheKey = new BookCacheKey("자바",1);

        var booksDto = bookSearchService.searchBooks("자바",1,10,"title");

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