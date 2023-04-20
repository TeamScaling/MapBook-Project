package com.scaling.libraryservice.search.controller;

import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchRestController {

    private final BookSearchService searchService;

  /* fixme : url "/books/author" -> "/books/search?target=author&query=남궁성"
        사라님 메소드의 url뿐만 아니라 기존의 search 메소드도 url 변경 필요 ("/books/search?target=title&query="자바의 정석")*/

    // 도서 검색
    @GetMapping(value = "/books/test")
    public ResponseEntity<RespBooksDto> searchBooks(@RequestParam("query") String query,
        @RequestParam("page") int page, @RequestParam("size") int size,
        @RequestParam("target") String target) {

        return ResponseEntity.ok(searchService.searchBooks(query, page, size, target));
    }

}
