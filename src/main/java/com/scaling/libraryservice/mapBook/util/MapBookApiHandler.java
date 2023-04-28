package com.scaling.libraryservice.mapBook.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.apiConnection.BExistConnection;
import com.scaling.libraryservice.caching.CustomCacheable;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import com.scaling.libraryservice.mapBook.dto.ApiStatus;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import com.scaling.libraryservice.caching.CacheKey;
import com.scaling.libraryservice.caching.CustomCacheManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MapBookApiHandler {

    private final ApiQuerySender apiQuerySender;
    private final ApiQueryBinder apiQueryBinder;

    private final CustomCacheManager<List<RespMapBookDto>> customCacheManager;

    @PostConstruct
    public void init() {

        Cache<CacheKey, List<RespMapBookDto>> mapBookCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

        customCacheManager.registerCaching(mapBookCache, this.getClass());
    }

    public void checkOpenApi() {

        BExistConnection bExistConn = new BExistConnection();
        ApiStatus status = BExistConnection.apiStatus;

        ResponseEntity<String> connection
            = apiQuerySender.singleQueryJson(bExistConn, "9788089365210");

        if (apiQueryBinder.bindBookExist(connection) == null) {
            status.closeAccess();
        } else {
            status.openAccess();
        }
    }

    @Timer @CustomCacheable
    public List<RespMapBookDto> matchMapBooks(List<LibraryDto> nearByLibraries,
        ReqMapBookDto reqMapBookDto) throws OpenApiException {

        Objects.requireNonNull(nearByLibraries);
        Objects.requireNonNull(reqMapBookDto);

        /*List<RespMapBookDto> cachingItem = customCacheManager.get(this.getClass(), reqMapBookDto);

        if (cachingItem != null) {

            return cachingItem;
        }*/

        List<BExistConnection> bExistConnections = nearByLibraries.stream()
            .map(n -> new BExistConnection(n.getLibNo())).toList();

        List<ResponseEntity<String>> responseEntities = apiQuerySender.multiQuery(
            bExistConnections,
            reqMapBookDto.getIsbn(),
            nearByLibraries.size());

        Map<Integer, ApiBookExistDto> bookExistMap
            = apiQueryBinder.bindBookExistMap(responseEntities);

        var result = mappingLoanableLib(nearByLibraries,bookExistMap);

       /* customCacheManager.put(this.getClass(), reqMapBookDto, result);*/

        return result;
    }

    private List<RespMapBookDto> mappingLoanableLib(List<LibraryDto> nearByLibraries,
        Map<Integer, ApiBookExistDto> bookExistMap) {
        List<RespMapBookDto> result = new ArrayList<>();

        for (LibraryDto l : nearByLibraries) {

            ApiBookExistDto apiBookExistDto = bookExistMap.get(l.getLibNo());

            if (apiBookExistDto != null) {
                result.add(new RespMapBookDto(apiBookExistDto, l));
            }
        }

        return result;

    }

    private List<BExistConnection> getBExistConns(List<LibraryDto> libraries) {
        return libraries.stream()
            .map(n -> new BExistConnection(n.getLibNo())).toList();
    }

    public BExistConnection getBExist(Integer libNo) {

        return new BExistConnection(libNo);
    }

}
