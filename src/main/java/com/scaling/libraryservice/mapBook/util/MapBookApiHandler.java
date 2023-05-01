package com.scaling.libraryservice.mapBook.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import com.scaling.libraryservice.commons.caching.CustomCacheable;
import com.scaling.libraryservice.commons.circuitBreaker.CircuitBreaker;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import com.scaling.libraryservice.mapBook.dto.ApiStatus;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
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
            CircuitBreaker.closeObserver(status);
        } else {
            status.openAccess();
        }
    }

    /**
     * 사용자가 원하는 도서와 사용자 주변의 도서관을 조합하여 대출 가능한 도서관들의 정보를 반환 한다.
     *
     * @param nearByLibraries 사용자 주변의 도서관 정보 Dto List
     * @param reqMapBookDto   사용자가 요청한 도서 정보와 위치 정보를 담는 Dto
     * @return 대출 가능한 도서관 정보를 담은 응답 Dto를 List 형태로 반환 한다.
     */
    @Timer
    @CustomCacheable
    public List<RespMapBookDto> matchMapBooks(List<LibraryDto> nearByLibraries,
        ReqMapBookDto reqMapBookDto) {

        Objects.requireNonNull(nearByLibraries);
        Objects.requireNonNull(reqMapBookDto);

        int hasBookCnt = nearByLibraries.stream().filter(l -> l.getHasBook().equals("Y")).toList()
            .size();

        if (hasBookCnt > 0) {
            List<BExistConn> bExistConns = nearByLibraries.stream()
                .map(n -> new BExistConn(n.getLibNo())).toList();

            List<ResponseEntity<String>> responseEntities = apiQuerySender.multiQuery(
                bExistConns,
                reqMapBookDto.getIsbn(),
                nearByLibraries.size());

            Map<Integer, ApiBookExistDto> bookExistMap
                = apiQueryBinder.bindBookExistMap(responseEntities);

            return mappingLoanableLib(nearByLibraries, bookExistMap);
        } else {

            return nearByLibraries.stream().map(RespMapBookDto::new).toList();
        }
    }

    /**
     * 대출 가능 Api 응답을 주변 도서관 정보와 연결 하여 대출 가능한 주변 도서관 정보를 담은 List를 반환 한다.
     *
     * @param nearByLibraries 사용자 주변 도서관 정보를 담은 Dto
     * @param bookExistMap 도서관 코드를 key, 대출 가능 Api 응답 Dto를 value로 가지는 Map
     * @return 대출 가능한 주변 도서관 정보에 대한 Dto List
     */
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

}
