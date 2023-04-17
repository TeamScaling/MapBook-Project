package com.scaling.libraryservice.controller;

import com.scaling.libraryservice.dto.ReqMapBookDto;
import com.scaling.libraryservice.dto.RespBookMapDto;
import com.scaling.libraryservice.service.MapSearchBookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String mapMarkerView(ModelMap model, @ModelAttribute ReqMapBookDto mapBookDto) {

        log.info("isbn : " + mapBookDto.getIsbn());

        List<RespBookMapDto> result
            = mapSearchBookService.loanAbleLibraries(mapBookDto);

        log.info(result+"");

        model.put("loanAble", result);

        return "mapMarker";
    }


    @GetMapping("/mapSearch/markers/json")
    @ResponseBody
    public ResponseEntity<List<RespBookMapDto>> mapMarkerJson(@ModelAttribute ReqMapBookDto mapBookDto) {

        List<RespBookMapDto> result
            = mapSearchBookService.loanAbleLibraries(mapBookDto);

        return ResponseEntity.ok(result);
    }


}
