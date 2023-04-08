package com.scaling.libraryservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapSearchController {

    @GetMapping("/mapSearch")
    public String basic(){

        return "mapBasic";
    }

    @GetMapping("/mapSearch/marker")
    public String mapMarker(){

        return "mapMarker";
    }

}
