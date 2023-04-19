package com.scaling.libraryservice.mapBook.service;

import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheService {

    private Map<Integer,Map<String, List<RespMapBookDto>>> mapBookCache;

    public CacheService(){
        this.mapBookCache = new ConcurrentHashMap<>();
    }

    public void get(int areaCd,String isbn13){

        List<RespMapBookDto> cachedMapBook = mapBookCache.get(areaCd).get(isbn13);

        /*if(cachedMapBook == null){
            putCache();

        }*/

    }

    public void putCache(RespMapBookDto respMapBookDto){

    }

}
