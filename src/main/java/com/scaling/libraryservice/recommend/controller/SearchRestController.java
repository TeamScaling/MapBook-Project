package com.scaling.libraryservice.recommend.controller;

import com.scaling.libraryservice.recommend.dto.ReqRecommendDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import com.scaling.libraryservice.recommend.service.RecommendService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<List<String>> getRecommends(@RequestBody ReqRecommendDto recommendDto){

        System.out.println("query.............."+recommendDto.getQuery());

        return ResponseEntity.ok(recommendService.getRecommendBook(recommendDto.getQuery()));
    }

}
