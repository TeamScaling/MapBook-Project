package com.scaling.libraryservice.controller;

import com.scaling.libraryservice.dto.RespSearchBooksDto;
import com.scaling.libraryservice.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchRestController {

    private final BookSearchService searchService;

    // 작가 검색 JPA
  /* fixme : url "/books/author" -> "/books/search?target=author&query=남궁성"
        사라님 메소드의 url뿐만 아니라 기존의 search 메소드도 url 변경 필요 ("/books/search?target=title&query="자바의 정석")*/
    @GetMapping(value = "/books/author/page")
    public ResponseEntity<RespSearchBooksDto> searchByAuthor(@RequestParam("query") String query,
        @RequestParam("page") int page, @RequestParam("size") int size) {

        return ResponseEntity.ok(searchService.searchByAuthor(query, page, size));
    }

    @GetMapping(value = "/books/title/page")
    public ResponseEntity<RespSearchBooksDto> searchByTitle(@RequestParam("query") String query,
        @RequestParam("page") int page, @RequestParam("size") int size) {

        return ResponseEntity.ok(searchService.searchByTitle(query, page, size));
    }


}
