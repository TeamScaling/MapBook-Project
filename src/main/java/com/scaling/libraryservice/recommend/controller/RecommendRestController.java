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

/**
 * 추천 도서 검색에 대한 Json 형태의 요청과 응답을 담당 한다.
 */
@RestController
@RequiredArgsConstructor
public class RecommendRestController {

    private final BookSearchService searchService;

    private final RecommendService recommendService;

    // 도서 검색
    @GetMapping(value = "/books/test")
    public ResponseEntity<RespBooksDto> searchBooks(@RequestParam("query") String query,
        @RequestParam("page") int page, @RequestParam("size") int size,
        @RequestParam("target") String target) {

        return ResponseEntity.ok(searchService.searchBooks(query, page, size, target));
    }

    /** 추천 도서 데이터를 얻기 위해 요청 된 검색어를 가지고 추천 도서 데이터를 JSON 형식으로 응답 한다.
     *
     * @param recommendDto 요청 검색어 query를 하나 담고 있는 사용자 요청 Dto
     * @return 추천 도서 제목을 String형태로 가지고 있는 List를 JSON 형태로 반환
     */
    @PostMapping("/books/recommend")
    public ResponseEntity<List<String>> getRecommends(@RequestBody ReqRecommendDto recommendDto){

        return ResponseEntity.ok(recommendService.getRecommendBook(recommendDto.getQuery()));
    }

}
