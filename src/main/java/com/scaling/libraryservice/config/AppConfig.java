package com.scaling.libraryservice.config;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        factory.setConnectTimeout(1000);
        factory.setReadTimeout(2000);

        return new RestTemplate(factory);
    }

    @Bean
    public Komoran komoran() {

        return new Komoran(DEFAULT_MODEL.FULL);
    }

//    @Bean
//    public Cache<CacheKey, List<LibraryDto>> libraryCache(){
//
//        return Caffeine.newBuilder()
//            .expireAfterWrite(1, TimeUnit.HOURS)
//            .maximumSize(1000)
//            .build();
//    }

    @Bean
    public  Cache<CacheKey, RespBooksDto> bookCache(){

        return Caffeine.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS).build();
    }

    @Bean
    public Cache<CacheKey, List<RespMapBookDto>> mapBookCache(){

        return Caffeine.newBuilder()
            .expireAfterWrite(6, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();
    }

    @Bean
    public Cache<CacheKey, List<String>> recCache(){

        return Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();
    }


}
