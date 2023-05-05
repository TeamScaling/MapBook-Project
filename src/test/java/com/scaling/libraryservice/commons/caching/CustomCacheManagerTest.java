package com.scaling.libraryservice.commons.caching;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.mapBook.util.ApiQuerySender;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class CustomCacheManagerTest {

    @Test @DisplayName("캐쉬 데이터가 사용되어 지면 유효기간이 갱신 된다.")
    public void load() throws InterruptedException {
        /* given */

        Cache<Integer, String> bookCache = Caffeine.newBuilder()
            .expireAfterAccess(5,TimeUnit.SECONDS)
            .maximumSize(1000)
            .build();
        /* when */
        
        bookCache.put(1,"1번");
        bookCache.put(2,"2번");
        /* then */

        Thread.sleep(1000*4);
        assertNotNull(bookCache.getIfPresent(1));
        Thread.sleep(1000*5);
        assertNull(bookCache.getIfPresent(1));
        assertNull(bookCache.getIfPresent(2));
    }
    
    @Test
    public void cache_server() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String jsonCacheData = new ObjectMapper().writeValueAsString("안녕하세요");

        // 백업 API에 요청을 보냅니다.
        HttpEntity<String> request = new HttpEntity<>(jsonCacheData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8086/test", request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Cache data for [{}] backed up successfully");
        } else {
            System.out.println("Failed to back up cache data for [{}]");
        }
    }

    @Test
    public void test_api_sender() throws JsonProcessingException {
        /* given */
        // 캐시 데이터를 JSON 형식으로 변환합니다.
        Map<Integer, String> cacheDataMap = new HashMap<>();


        for(int i=0; i<100; i++){
            cacheDataMap.put(i,i+"번");
        }


        String jsonCacheData = new ObjectMapper().writeValueAsString(cacheDataMap);

        System.out.println(jsonCacheData);

        ApiQuerySender sender = new ApiQuerySender(new RestTemplate());
        /* when */

        sender.sendPost(jsonCacheData,"http://localhost:8086/test");
        /* then */
    }

}