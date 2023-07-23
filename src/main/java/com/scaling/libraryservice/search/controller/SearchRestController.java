package com.scaling.libraryservice.search.controller;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.mapBook.dto.TestingBookDto;
import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SearchRestController {

    private final BookSearchService bookSearchService;

    @Timer
    @RequestMapping(value = "/books/autocomplete")
    public @ResponseBody Map<String, Object> autocomplete
        (@RequestParam Map<String, Object> paramMap) throws Exception {

        RespBooksDto result = bookSearchService.searchBooks(
            new ReqBookDto(paramMap.get("value") + "", 1, 5), 3);

        paramMap.put("resultList", result.getDocuments());

        return paramMap;
    }

    @GetMapping("/books/search/test")
    public ResponseEntity<RespBooksDto> restSearchBook(@NonNull @RequestBody TestingBookDto testingBookDto) {

        RespBooksDto result = bookSearchService.searchBooks(
            new ReqBookDto(testingBookDto.getBookName(), 1,10),3);

        if (result.getDocuments().isEmpty()) {
            log.info("[Not Found]This book is Not Found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

}
