package com.scaling.libraryservice.search.controller;

import com.google.gson.Gson;
import com.scaling.libraryservice.search.dto.RelationWords;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.RespRecommend;
import com.scaling.libraryservice.search.dto.TitleDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import com.scaling.libraryservice.search.service.RecommendService;
import com.scaling.libraryservice.search.service.RelationWordService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchRestController {

    private final BookSearchService searchService;
    private final RecommendService recommendService;
    private final RelationWordService relationWordService;

  /* fixme : url "/books/author" -> "/books/search?target=author&query=남궁성"
        사라님 메소드의 url뿐만 아니라 기존의 search 메소드도 url 변경 필요 ("/books/search?target=title&query="자바의 정석")*/

    // 도서 검색
    @PostMapping(value = "/books/test")
    public ResponseEntity<RespBooksDto> searchBooks(@RequestParam("query") String query,
        @RequestParam("page") int page, @RequestParam("size") int size,
        @RequestParam("target") String target) {

        return ResponseEntity.ok(searchService.searchBooks(query, page, size, target));
    }

    // 도서 추천
    @PostMapping(value = "/books/recommend")
    public ResponseEntity<RespRecommend> getRecommendedBooks(HttpServletRequest request) {
        String searchResultTitleJson = (String) request.getSession().getAttribute("searchResultTitle");

        System.out.println("============================");
        log.info("세션에 저장된 값 불러오기 : " + searchResultTitleJson);
        System.out.println("============================");

        Gson gson = new Gson();
        RespBooksDto searchResultTitle = gson.fromJson(searchResultTitleJson, RespBooksDto.class);

        System.out.println("============================");
        log.info("세션에 저장된 값 불러오기 : " + searchResultTitle);
        System.out.println("============================");

        RespRecommend recommends = recommendService.getRecommendBook(searchResultTitle);

        System.out.println("============================");
        log.info("recommends에 저장된 값 불러오기 : " + recommends);
        System.out.println("============================");

        return ResponseEntity.ok(recommends);

    }




    // 연관 검색어

    @PostMapping(value = "/books/relation")
    public ResponseEntity<RelationWords> getRelatedWords(@RequestBody RespBooksDto searchResult) {
        RespRecommend recommends = recommendService.getRecommendBook(searchResult);
        RelationWords relationWords = relationWordService.getRelatedWords(recommends.getRecommendBooks());
        return ResponseEntity.ok(relationWords);
    }


//    @GetMapping(value = "/books/relation")
//    public ResponseEntity<RelationWords> getRelatedWords(@RequestParam("query") String query,
//        @RequestParam("page") int page, @RequestParam("size") int size,
//        @RequestParam("target") String target) {
//
//        RespBooksDto searchResult = searchService.searchBooks(query, page, size, target);
//        RespRecommend recommends = recommendService.getRecommendBook(searchResult);
//        RelationWords relationWords = relationWordService.getRelatedWords(recommends.getRecommendBooks());
//
//
//        return ResponseEntity.ok(relationWords);
//    }

}
