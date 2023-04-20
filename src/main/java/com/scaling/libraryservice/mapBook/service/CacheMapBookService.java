package com.scaling.libraryservice.mapBook.service;

import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.util.ApiQueryBinder;
import com.scaling.libraryservice.mapBook.util.ApiQuerySender;
import com.scaling.libraryservice.mapBook.util.MapBookMatcher;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheMapBookService {


    private Map<ReqMapBookDto, List<RespMapBookDto>> cachedReqMapBook;
    private final LibraryFindService libraryFindService;
    private final ApiQuerySender apiQuerySender;
    private final ApiQueryBinder apiQueryBinder;
    private final MapBookMatcher mapBookMatcher;


    @PostConstruct
    public void init() {
        this.cachedReqMapBook = new ConcurrentHashMap<>();
    }

    public List<RespMapBookDto> getMapBooks(ReqMapBookDto mapBookDto) {

        String isbn13 = mapBookDto.getIsbn();

        if (cachedReqMapBook.containsKey(mapBookDto)) {

            log.info(mapBookDto.getIsbn() + "  cache hit !!!!!!!!!!!!!!!!");
            return cachedReqMapBook.get(mapBookDto);

        } else {

            log.info(mapBookDto.getIsbn() + "  cache miss...............");

            List<LibraryDto> nearByLibraries
                = libraryFindService.getNearByLibraries(mapBookDto);

            List<ResponseEntity<String>> responseEntities
                = apiQuerySender.multiQuery(nearByLibraries, isbn13, 10);

            Map<Integer, ApiBookExistDto> bookExistMap
                = apiQueryBinder.bindBookExistMap(responseEntities);

            List<RespMapBookDto> result
                = mapBookMatcher.matchMapBooks(nearByLibraries, bookExistMap);

            cachedReqMapBook.put(mapBookDto, result);

            return result;
        }
    }


}
