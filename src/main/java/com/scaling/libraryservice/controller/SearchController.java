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
  //fixme : url 변경 필요성 생김 ("/books/search?target=title&query="자바의 정석")*
  @GetMapping(value = "/books/search")
  public Page<RespBooksDto> search(@RequestParam("query") String query,
      @RequestParam("page") int page, @RequestParam("size") int size) {

    return searchService.searchBookPage(query, page, size);
  }

  @GetMapping(value = "/books/search-test")
  public ResponseEntity<RespBooksDto> searchBook(@RequestParam("query") String query){

    return ResponseEntity.ok(searchService.searchBook(query));
  }

  // 작가 검색 JPA
  //fixme : searchAuthor -> searchByAuthor는 어떨까요? <다른 메소드도 바뀌긴 해야겠네요(searchByTtile)>
  /*fixme : url "/books/author" -> "/books/search?target=author&query=남궁성"
        사라님 메소드의 url뿐만 아니라 기존의 search 메소드도 url 변경 필요 ("/books/search?target=title&query="자바의 정석")*/
  @GetMapping(value = "/books/author")
  public ResponseEntity<RespBooksDto> searchAuthor(@RequestParam("query") String query){
    System.out.println("작가찾기 "+query);
    return ResponseEntity.ok(searchService.searchAuthor(query));
  }





}
