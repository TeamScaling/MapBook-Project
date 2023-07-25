package com.scaling.libraryservice.commons.tester;

import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TesterRestController {

    private final BookSearchService searchService;


    @PostMapping("/books/search/tester")
    public String SearchBook(@RequestParam(value = "query", required = false) String query,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "10") int size) {

        RespBooksDto searchResult = searchService.searchBooks(new ReqBookDto(query,page,size),3,true);

        return "search/searchResult";
    }

}
