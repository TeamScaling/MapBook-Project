package com.scaling.libraryservice.mapBook.util;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestCache {

    private final CacheManager cacheManager;

    @Cacheable(value = "myCache")
    public int test(int a){

        var cache= cacheManager.getCache("myCache");

        System.out.println();

        return a+1000;
    }

}
