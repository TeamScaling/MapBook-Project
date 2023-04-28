package com.scaling.libraryservice.search.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.caching.CustomCacheManager;
import com.scaling.libraryservice.search.cacheKey.BookHashKey;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.util.TitleTokenizer;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@SpringBootTest
class CacheProxyTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TitleTokenizer titleTokenizer;

    @Autowired
    private CustomCacheManager<RespBooksDto> cacheManager;

    @Test
    public void load(){
        /* given */

        ProxyFactoryBean pfBean = new ProxyFactoryBean();

        pfBean.setTarget(new BookSearchService(bookRepository,titleTokenizer,cacheManager));

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();

        pointcut.setMappedName("searchBooks2");

        Cache<BookHashKey, RespBooksDto> bookCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

        CacheProxy<BookHashKey, RespBooksDto> cacheProxy
            = new CacheProxy<>(bookCache);

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut,cacheProxy));

        BookSearchService search = (BookSearchService) pfBean.getObject();

        System.out.println(search.searchBooks2("자바",1,10,"title"));

        /* when */

        /* then */
    }

}