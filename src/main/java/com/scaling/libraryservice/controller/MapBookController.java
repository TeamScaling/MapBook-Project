package com.scaling.libraryservice.controller;

import com.scaling.libraryservice.dto.ApiBookExistDto;
import com.scaling.libraryservice.dto.LibraryDto;
import com.scaling.libraryservice.dto.ReqMapBookDto;
import com.scaling.libraryservice.dto.RespMapBookDto;
import com.scaling.libraryservice.service.ApiBindService;
import com.scaling.libraryservice.service.LibraryFindService;
import com.scaling.libraryservice.service.ApiQueryService;
import com.scaling.libraryservice.service.MapBookService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MapBookController {

    private final MapBookService mapBookService;

    private final ApiQueryService apiQueryService;

    private final LibraryFindService libraryFindService;

    private final ApiBindService apiBindService;

    @GetMapping("/mapBook/search")
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

        return "mapMarker";
    }

}
