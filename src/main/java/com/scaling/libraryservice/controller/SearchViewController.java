package com.scaling.libraryservice.controller;

import com.scaling.libraryservice.dto.RespSearchBooksDto;
import com.scaling.libraryservice.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@Slf4j
public class SearchViewController {

    private final BookSearchService searchService;

    @GetMapping("/")
    public String home() {
        return "search/home";
    }

    @GetMapping("/books/search")
    public String SearchBook(@RequestParam(value = "query", required = false) String query,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "10") int size, ModelMap model) {

        if (!query.isEmpty()) {

            RespSearchBooksDto searchResult = searchService.searchByTitle(query, page, size);

            model.put("searchResult", searchResult);
            model.put("totalPages", searchResult.getMeta().getTotalPages());
            model.put("size", searchResult.getMeta().getTotalElements());

        }
        //fixme : query가 empty일 때, 사용자가 공백 검색을 못하게 alert를 띄운다.

        return "search/searchResult";
    }
}
