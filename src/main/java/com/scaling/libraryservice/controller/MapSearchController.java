package com.scaling.libraryservice.controller;

import com.scaling.libraryservice.dto.RespBookMapDto;
import com.scaling.libraryservice.service.BookOpenApiService;
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
public class MapSearchController {

    private final BookOpenApiService bookOpenApiService;

    @GetMapping("/mapSearch")
    public String basic(){

        return "mapBasic";
    }

    @GetMapping("/mapSearch/marker")
    public String mapMarkerView(ModelMap model){

        String isbn = "9788994492032";
        String area = "성남";

        List<RespBookMapDto> result
            = bookOpenApiService.getMarkerData(isbn,area);

        model.put("data",result);

        return "mapMarker3";
    }

    @GetMapping("/mapSearch/marker/json")
    @ResponseBody
    public ResponseEntity<List<RespBookMapDto>> mapMarkerJson(@RequestParam("isbn") String isbn){

        String area = "성남";

        List<RespBookMapDto> result
            = bookOpenApiService.getMarkerData(isbn,area);

        System.out.println(isbn);

        return ResponseEntity.ok(result);
    }

}
