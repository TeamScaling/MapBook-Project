package com.scaling.libraryservice.search.controller;

import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.RespRecommend;
import com.scaling.libraryservice.search.service.BookSearchService;
import com.scaling.libraryservice.search.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchRestController {

    private final BookSearchService searchService;

    private final RecommendService recommendService;

    // 도서 검색
    @GetMapping(value = "/books/test")
    public ResponseEntity<RespBooksDto> searchBooks(@RequestParam("query") String query,
        @RequestParam("page") int page, @RequestParam("size") int size,
        @RequestParam("target") String target) {

        return ResponseEntity.ok(searchService.searchBooks(query, page, size, target));
    }

    @PostMapping("/books/recommend")
    public ResponseEntity<RespRecommend> getRecommends(RespBooksDto searchResult){


        System.out.println(searchResult);

        return null;
    }

}
