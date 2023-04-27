package com.scaling.libraryservice.mapBook.controller;

import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import com.scaling.libraryservice.mapBook.dto.ApiStatus;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.service.CachedMapBookManager;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.util.ApiQueryBinder;
import com.scaling.libraryservice.mapBook.util.ApiQuerySender;
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

    private final ApiQuerySender apiQuerySender;
    private final ApiQueryBinder apiQueryBinder;
    private final CachedMapBookManager cachedMapBookManager;
    private final MapBookApiHandler mapBookApiHandler;

    @PostConstruct
    public void init() {
        mapBookApiHandler.isAccessOpenApi();
    }

    @GetMapping("/books/mapBook/search")
    public String getLoanableMapBookMarkers(ModelMap model,
        @ModelAttribute ReqMapBookDto mapBookDto) {

        mapBookDto.updateAreaCd();

        if (!new LibraryDto().getApiStatus().apiAccessible()) {
            return getHasBookMarkers(model, mapBookDto);
        }

        List<RespMapBookDto> mapBooks = cachedMapBookManager.getMapBooks(mapBookDto);

        model.put("mapBooks", mapBooks);

        return "mapBook/mapBookMarker";
    }

    @GetMapping("/books/hasBookLibs")
    public String getHasBookMarkers(ModelMap model,
        @ModelAttribute ReqMapBookDto mapBookDto) {

        List<RespMapBookDto> hasBookLibs = cachedMapBookManager.getMapBooks(mapBookDto);

        model.put("hasBookLibs", hasBookLibs);

        return "mapBook/hasLibMarker";
    }

    public String getLoanableMapBookSingle(ModelMap model,
        @ModelAttribute ReqMapBookDto mapBookDto) {

        LibraryDto nearestLibrary
            = LibraryFindService.findNearestLibraryWithCoordinate(mapBookDto);

        var responseEntity
            = apiQuerySender.singleQueryJson(nearestLibrary, mapBookDto.getIsbn());

        ApiBookExistDto bookExist
            = apiQueryBinder.bindBookExist(responseEntity);

        model.put("mapBooks", bookExist);

        return "mapBook/mapBookMarker";
    }


}
