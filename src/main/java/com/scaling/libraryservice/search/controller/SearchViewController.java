package com.scaling.libraryservice.search.controller;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.search.dto.RelationWords;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.RespRecommend;
import com.scaling.libraryservice.search.service.BookSearchService;
import com.scaling.libraryservice.search.service.RecommendService;
import com.scaling.libraryservice.search.service.RelationWordService;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Controller
@Slf4j
public class SearchViewController {

    private final BookSearchService searchService;


    @GetMapping("/")
    public String home() {
        return "search/home";
    }

//    @GetMapping("/books/search")
//    @Timer
//    public String SearchBook2(@RequestParam(value = "query", required = false) String query,
//        @RequestParam(value = "page", defaultValue = "1") int page,
//        @RequestParam(value = "size", defaultValue = "10") int size,
//        @RequestParam(value = "target", defaultValue = "title") String target, ModelMap model) {
//
//        if (!query.isEmpty()) {
//            RespBooksDto searchResult = searchService.searchBooks2(query, page, size,target);
//
//
//            model.put("searchResult", searchResult);
//            model.put("totalPages", searchResult.getMeta().getTotalPages());
//            model.put("size", searchResult.getMeta().getTotalElements());
//        }
//        //fixme : query가 empty일 때, 사용자가 공백 검색을 못하게 alert를 띄운다.
//
//        return "search/searchResult";
//    }

    @GetMapping("/books/search")
    @Timer
    public String SearchBook(@RequestParam(value = "query", required = false) String query,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "target", defaultValue = "title") String target,
        HttpSession session, ModelMap model) {

        if (!query.isEmpty()) {
            RespBooksDto searchResult = searchService.searchBooks3(query, page, size, target, session);


            model.put("searchResult", searchResult);
            model.put("totalPages", searchResult.getMeta().getTotalPages());
            model.put("size", searchResult.getMeta().getTotalElements());

            //세션에 내용저장
            session.setAttribute("searchResultTitle", searchResult.getTitle());
            session.setMaxInactiveInterval(1800); // 세션 만료 기한 30분
            log.info("세션에 저장된값 확인하기 : " + searchResult);
        }
        //fixme : query가 empty일 때, 사용자가 공백 검색을 못하게 alert를 띄운다.

        return "search/searchResult";
    }
}
