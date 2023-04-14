package com.scaling.libraryservice.controller;

import com.scaling.libraryservice.dto.RespBookMapDto;
import com.scaling.libraryservice.service.MapSearchBookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MapBookController {

    private final MapSearchBookService mapSearchBookService;

    @GetMapping("/mapSearch")
    public String mapSearch() {

        return "mapSearch";
    }

    @GetMapping("/mapSearch/markers")
    public String mapMarkerView(ModelMap model, @RequestParam("isbn") String isbn) {

        String area = "성남";

        log.info("isbn : " + isbn);

        List<RespBookMapDto> result
            = mapSearchBookService.loanAbleLibraries(isbn, area);


        model.put("loanAble", result);

        return "mapMarker";
    }


    @GetMapping("/mapSearch/markers/json")
    @ResponseBody
    public ResponseEntity<List<RespBookMapDto>> mapMarkerJson(@RequestParam("isbn") String isbn,
        @RequestParam("area") String area) {

        List<RespBookMapDto> result
            = mapSearchBookService.loanAbleLibraries(isbn, area);

        return ResponseEntity.ok(result);
    }



}
