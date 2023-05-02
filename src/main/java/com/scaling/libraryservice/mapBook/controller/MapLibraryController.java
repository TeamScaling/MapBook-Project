package com.scaling.libraryservice.mapBook.controller;

import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.service.LibraryMetaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * {@link MapLibraryController} 클래스는 도서관 지도 관련 요청을 처리합니다.
 * 이 컨트롤러는 도서관 정보를 조회하고, 지역 코드를 사용하여 특정 지역의 도서관 목록을 반환합니다.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/mapLibrary")
public class MapLibraryController {

    private final LibraryMetaService libraryMetaService;

    private final LibraryFindService libraryFindService;

    /**
     * 도서관 지도 페이지를 반환합니다.
     *
     * @param model 전국 도서관 메타 정보를 담을 ModelMap 객체
     * @return 뷰 이름
     */
    @GetMapping("")
    public String mapBookView(ModelMap model) {

        model.put("libraryMeta", libraryMetaService.getLibraryMeta());

        return "libraryView/viewSearch";
    }

    /**
     * 전체 도서관 목록 및 정보를 반환합니다.
     *
     * @param model 전체 도서관의 정보를 담은 ModelMap 객체
     * @return 뷰 이름
     */
    @PostMapping("/all")
    public String getLibraryAll(ModelMap model) {

        List<LibraryDto> libraries = LibraryFindService.getAllLibraries();

        model.put("libraries", libraries);

        return "mapBook/LibraryMarkers";
    }

    /**
     * 주어진 지역 코드에 해당하는 도서관 목록을 반환합니다.
     *
     * @param model 조건에 맞는 도서관 목록을 담을 ModelMap 객체
     * @param areaCd 지역 코드
     * @return 뷰 이름
     */
    @GetMapping("/search")
    public String getLibrariesByAreaCd(ModelMap model, @RequestParam("areaCd") int areaCd) {

        List<LibraryDto> libraries
            = libraryFindService.getNearByAllLibraries(areaCd);

        if (!libraries.isEmpty()) {

            LibraryDto libraryDto = libraries.get(0);

            String metaStr = areaCd + " / " + libraryDto.getFullAreaNm();

            model.put("meta", metaStr);
        }

        model.put("libraries", libraries);

        return "mapBook/LibraryMarkers";
    }

}
