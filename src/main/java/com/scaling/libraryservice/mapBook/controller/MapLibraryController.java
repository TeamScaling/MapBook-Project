package com.scaling.libraryservice.mapBook.controller;

import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mapLibrary")
public class MapLibraryController {

    private final LibraryFindService libraryFindService;

    @GetMapping("")
    public String mapBookView(ModelMap model) {

        model.put("libraryMeta", libraryFindService.getLibraryMeta());

        return "libraryView/viewSearch";
    }

    @PostMapping("/all")
    public String getLibraryAll(ModelMap model) {

        List<LibraryDto> libraries = libraryFindService.getLibraries();

        model.put("libraries", libraries);

        return "mapBook/LibraryMarkers";
    }

    @GetMapping("/search")
    public String getLibrariesByAreaCd(ModelMap model, @RequestParam("areaCd") int areaCd) {

        List<LibraryDto> libraries
            = libraryFindService.findLibraries(areaCd);

        if (!libraries.isEmpty()) {

            LibraryDto libraryDto = libraries.get(0);

            String metaStr = areaCd + " / " + libraryDto.getFullAreaNm();

            model.put("meta", metaStr);
        }

        model.put("libraries", libraries);

        return "mapBook/LibraryMarkers";
    }

}
