package com.scaling.libraryservice.mapBook.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.util.ApiQueryBinder;
import com.scaling.libraryservice.mapBook.util.ApiQuerySender;
import com.scaling.libraryservice.mapBook.util.MapBookMatcher;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheMapBookService {

    private Cache<ReqMapBookDto, List<RespMapBookDto>> cacheManager;
    private final LibraryFindService libraryFindService;
    private final ApiQuerySender apiQuerySender;
    private final ApiQueryBinder apiQueryBinder;
    private final MapBookMatcher mapBookMatcher;

    @PostConstruct
    public void init() {
        this.cacheManager = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

    }

    @Timer
    public List<RespMapBookDto> getMapBooks(ReqMapBookDto mapBookDto) {

        List<RespMapBookDto> value = cacheManager.getIfPresent(mapBookDto);

        if (value != null) {

            log.info(mapBookDto.getIsbn() + "  cache hit !!!!!!!!!!!!!!!!");

        } else {

            log.info(mapBookDto.getIsbn() + "  cache miss...............");

            List<LibraryDto> nearByLibraries = libraryFindService.getNearByLibraries(mapBookDto);

            Map<Integer, ApiBookExistDto> bookExistMap = apiQueryBinder.bindBookExistMap(
                apiQuerySender.multiQuery(
                    nearByLibraries,
                    mapBookDto.getIsbn(),
                    nearByLibraries.size()));

            value = mapBookMatcher.matchMapBooks(nearByLibraries, bookExistMap);

            cacheManager.put(mapBookDto, value);
        }

        return value;
    }


}
