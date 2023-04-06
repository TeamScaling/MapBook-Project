package com.scaling.libraryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaling.libraryservice.dto.RespBooksDto;
import com.scaling.libraryservice.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

  private final BookSearchService searchService;

  @GetMapping(value = "/books/search")
  public Page<RespBooksDto> search(@RequestParam("query") String query,
      @RequestParam("page") int page, @RequestParam("size") int size) {

    return searchService.searchBook(query, page, size);
  }


}
