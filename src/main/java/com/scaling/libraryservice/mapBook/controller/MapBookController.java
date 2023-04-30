package com.scaling.libraryservice.mapBook.controller;

import com.scaling.libraryservice.commons.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.circuitBreaker.Substitutable;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.util.MapBookApiHandler;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MapBookController {

    private final MapBookApiHandler mapBookApiHandler;

    private final LibraryFindService libraryFindService;

    @PostConstruct
    public void init() {
        mapBookApiHandler.checkOpenApi();
    }

    @GetMapping("/books/mapBook/search")
    @Substitutable(origin = BExistConn.class, substitute = "getHasBookMarkers")
    public String getMapBooks(ModelMap model, @ModelAttribute ReqMapBookDto reqMapBookDto) {

        reqMapBookDto.updateAreaCd();

        List<LibraryDto> nearbyLibraries = libraryFindService.getNearByLibraries(reqMapBookDto);

        List<RespMapBookDto> mapBooks = mapBookApiHandler.matchMapBooks(nearbyLibraries,
            reqMapBookDto);

        model.put("mapBooks", mapBooks);

        return "mapBook/mapBookMarker";
    }

    // getMapBook가 이용하는 OpenAPI 장애시 circuitBreakerAspect에 의해 호출 됨
    public String getHasBookMarkers(ModelMap model,
        @ModelAttribute ReqMapBookDto mapBookDto) {

        List<LibraryDto> nearbyLibraries = libraryFindService.getNearByLibraries(mapBookDto);

        List<RespMapBookDto> hasBookLibs = nearbyLibraries.stream().map(RespMapBookDto::new)
            .toList();

        model.put("hasBookLibs", hasBookLibs);

        return "mapBook/hasLibMarker";
    }


}
