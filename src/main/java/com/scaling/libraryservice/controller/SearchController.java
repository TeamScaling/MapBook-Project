package com.scaling.libraryservice.controller;

import com.scaling.libraryservice.dto.RespBooksDto;
import com.scaling.libraryservice.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

  private final BookSearchService searchService;

  // 기존 검색 + 페이징
  @GetMapping(value = "/books/search")
  public Page<RespBooksDto> search(@RequestParam("query") String query,
      @RequestParam("page") int page, @RequestParam("size") int size) {

    return searchService.searchBookPage(query, page, size);
  }

  @GetMapping(value = "/books/search-test")
  public ResponseEntity<RespBooksDto> searchBook(@RequestParam("query") String query){

    return ResponseEntity.ok(searchService.searchBook(query));
  }


}
