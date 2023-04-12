package com.scaling.libraryservice.controller;

import com.scaling.libraryservice.dto.RespBookMapDto;
import com.scaling.libraryservice.service.BookMapService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MapSearchController {

    private final BookMapService bookMapService;

    @GetMapping("/mapSearch")
    public String mapSearch(){

        return "mapSearch";
    }

    @PostMapping("/mapSearch/marker")
    public String mapMarkerView(ModelMap model,String isbn){

        String area = "성남";

        List<RespBookMapDto> result
            = bookMapService.loanAbleLibrary(isbn,area);

        log.info(result+"");

        model.put("loanAble",result);

        return "mapMarker";
    }

    @GetMapping("/mapSearch/marker/json")
    @ResponseBody
    public ResponseEntity<List<RespBookMapDto>> mapMarkerJson(@RequestParam("isbn") String isbn){

        String area = "성남";

        List<RespBookMapDto> result
            = bookMapService.loanAbleLibrary(isbn,area);

        System.out.println(isbn);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/mapSearch/marker/json2")
    @ResponseBody
    public ResponseEntity<List<RespBookMapDto>> mapMarkerJson2(@RequestParam("isbn") String isbn){

        String area = "성남";

        List<RespBookMapDto> result
            = bookMapService.queryExistLocation(isbn,area);

        System.out.println(isbn);

        return ResponseEntity.ok(result);
    }

}
