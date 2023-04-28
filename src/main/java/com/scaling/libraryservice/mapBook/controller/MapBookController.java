package com.scaling.libraryservice.mapBook.controller;

import com.scaling.libraryservice.mapBook.apiConnection.BExistConnection;
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
    public String getMapBooks(ModelMap model,
        @ModelAttribute ReqMapBookDto reqMapBookDto) {

        reqMapBookDto.updateAreaCd();

        if (!BExistConnection.apiStatus.apiAccessible()) {
            return getHasBookMarkers(model, reqMapBookDto);
        }

        List<LibraryDto> nearbyLibraries = libraryFindService.getNearByLibraries(
            reqMapBookDto);

        List<RespMapBookDto> mapBooks = mapBookApiHandler.matchMapBooks(
            nearbyLibraries,reqMapBookDto);

        model.put("mapBooks", mapBooks);

        return "mapBook/mapBookMarker";
    }

    @GetMapping("/books/hasBookLibs")
    public String getHasBookMarkers(ModelMap model,
        @ModelAttribute ReqMapBookDto mapBookDto) {

        List<LibraryDto> nearbyLibraries = libraryFindService.getNearByLibraries(
            mapBookDto);

        List<RespMapBookDto> hasBookLibs = mapBookApiHandler.matchMapBooks(
            nearbyLibraries,mapBookDto);

        model.put("hasBookLibs", hasBookLibs);

        return "mapBook/hasLibMarker";
    }

    /*public String getLoanableMapBookSingle(ModelMap model,
        @ModelAttribute ReqMapBookDto mapBookDto) {

        LibraryDto nearestLibrary
            = LibraryFindService.findNearestLibraryWithCoordinate(mapBookDto);

        var responseEntity
            = apiQuerySender.singleQueryJson(nearestLibrary, mapBookDto.getIsbn());

        ApiBookExistDto bookExist
            = apiQueryBinder.bindBookExist(responseEntity);

        model.put("mapBooks", bookExist);

        return "mapBook/mapBookMarker";
    }*/


}
