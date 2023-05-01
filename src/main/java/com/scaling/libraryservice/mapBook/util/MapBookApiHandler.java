package com.scaling.libraryservice.mapBook.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.commons.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.caching.CustomCacheable;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import com.scaling.libraryservice.mapBook.dto.ApiStatus;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.commons.caching.CustomCacheManager;
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

        BExistConn bExistConn = new BExistConn();
        ApiStatus status = BExistConn.apiStatus;

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

        int hasBookCnt = nearByLibraries.stream().filter(l -> l.getHasBook().equals("Y")).toList().size();

        if(hasBookCnt >0){
            List<BExistConn> bExistConns = nearByLibraries.stream()
                .map(n -> new BExistConn(n.getLibNo())).toList();

            List<ResponseEntity<String>> responseEntities = apiQuerySender.multiQuery(
                bExistConns,
                reqMapBookDto.getIsbn(),
                nearByLibraries.size());

            Map<Integer, ApiBookExistDto> bookExistMap
                = apiQueryBinder.bindBookExistMap(responseEntities);

            return mappingLoanableLib(nearByLibraries,bookExistMap);
        }else{

            return nearByLibraries.stream().map(RespMapBookDto::new).toList();
        }
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

    private List<BExistConn> getBExistConns(List<LibraryDto> libraries) {
        return libraries.stream()
            .map(n -> new BExistConn(n.getLibNo())).toList();
    }

    public BExistConn getBExist(Integer libNo) {

        return new BExistConn(libNo);
    }

}
