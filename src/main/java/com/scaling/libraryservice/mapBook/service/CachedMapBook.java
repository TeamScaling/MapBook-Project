package com.scaling.libraryservice.mapBook.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.mapBook.apiConnection.BExistConnection;
import com.scaling.libraryservice.mapBook.dto.ApiStatus;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.util.MapBookApiHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class CachedMapBook {

    private Cache<ReqMapBookDto, List<RespMapBookDto>> mapBookCache;
    private final LibraryFindService libraryFindService;
    private final MapBookApiHandler mapBookApiHandler;

    @PostConstruct
    public void init() {
        this.mapBookCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

    }

    @Timer
    public List<RespMapBookDto> getMapBooks(ReqMapBookDto reqMapBookDto) {

        List<RespMapBookDto> value = mapBookCache.getIfPresent(reqMapBookDto);

        if (value != null) {

            log.info(reqMapBookDto.getIsbn() + "  cache hit !!!!!!!!!!!!!!!!");

        } else {

            log.info(reqMapBookDto.getIsbn() + "  cache miss...............");

            List<LibraryDto> nearByLibraries = libraryFindService.getNearByLibraries(reqMapBookDto);

            if (!nearByLibraries.isEmpty()) {

                ApiStatus status = BExistConnection.apiStatus;

                if (!status.apiAccessible()) {

                    log.info("{} is not accessible then Service is changed hasBookLib service",
                        status.getApiUri());

                    return nearByLibraries.stream().map(RespMapBookDto::new).toList();
                } else {

                    value = mapBookApiHandler.matchMapBooks(nearByLibraries, reqMapBookDto);
                }
            } else {
                value = new ArrayList<>();
            }

            mapBookCache.put(reqMapBookDto, value);
        }

        return value;
    }


}
