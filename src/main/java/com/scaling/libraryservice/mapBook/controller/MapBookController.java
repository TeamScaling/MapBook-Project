package com.scaling.libraryservice.mapBook.controller;

import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.service.ApiBindService;
import com.scaling.libraryservice.mapBook.service.ApiQueryService;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.service.MapBookService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MapBookController {

    private final MapBookService mapBookService;
    private final ApiQueryService apiQueryService;
    private final LibraryFindService libraryFindService;
    private final ApiBindService apiBindService;

    @GetMapping("/map-book/search")
    public String getLoanableMapBookMarkers(ModelMap model,
        @ModelAttribute ReqMapBookDto mapBookDto) {

        // 사용자의 위치 정보를 바탕으로 주변 도서관 목록을 검색 한다.
        List<LibraryDto> nearByLibraries
            = libraryFindService.findNearByLibraries(mapBookDto);

        // 사용자의 주변 도서관을 대상으로 도서가 대출 가능 한지 확인 한다.
        List<ResponseEntity<String>> loanableLibraries
            = apiQueryService.multiQuery(nearByLibraries, mapBookDto.getIsbn(), 10);

        // 대출 가능 도서 응답 데이터를 담는 객체 map으로 바인딩 한다.
        Map<Integer, ApiBookExistDto> apiBookExistMap
            = apiBindService.getBookExistMap(loanableLibraries);

        // 주변 도서관 목록과 대출 가능 여부 데이터를 조합하여 map marker를 만들기 위한 결과를 만든다.
        List<RespMapBookDto> mapBooks
            = mapBookService.getMapBooks(nearByLibraries, apiBookExistMap);

        model.put("mapBooks", mapBooks);

        return "mapBook/mapBookMarker";
    }

    @GetMapping("/library/searchView")
    public String mapBookView(ModelMap model) {

        model.put("libraryMeta", libraryFindService.getLibraryMeta());

        return "libraryView/viewSearch";
    }

    @PostMapping("/library/all")
    public String getLibraryAll(ModelMap model) {

        List<LibraryDto> libraries = libraryFindService.getLibraries();

        model.put("libraries", libraries);

        return "mapBook/LibraryMarkers";
    }

    @GetMapping("/library/mapSearch")
    public String getLibrariesByAreaCd(ModelMap model, @RequestParam("areaCd") int areaCd) {

        List<LibraryDto> libraries
            = libraryFindService.findLibrariesByAreaCd(areaCd);

        if (!libraries.isEmpty()) {

            LibraryDto libraryDto = libraries.get(0);

            String metaStr = areaCd + " / " + libraryDto.getFullAreaNm();

            model.put("meta", metaStr);
        }

        model.put("libraries", libraries);

        return "mapBook/LibraryMarkers";
    }

}
