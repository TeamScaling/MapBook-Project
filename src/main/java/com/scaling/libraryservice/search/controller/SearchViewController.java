package com.scaling.libraryservice.search.controller;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.search.dto.RelationWords;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.RespRecommend;
import com.scaling.libraryservice.search.service.BookSearchService;
import com.scaling.libraryservice.search.service.RecommendService;
import com.scaling.libraryservice.search.service.RelationWordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@Slf4j
public class SearchViewController {

    private final BookSearchService searchService;

    private final RelationWordService relationWordService;

    private final RecommendService recommendService;

    @GetMapping("/")
    public String home() {
        return "search/home";
    }

    @GetMapping("/books/search")
    @Timer
    public String SearchBook(@RequestParam(value = "query", required = false) String query,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "target", defaultValue = "title") String target, ModelMap model) {

        if (!query.isEmpty()) {
            RespBooksDto searchResult = searchService.searchBooks2(query,page,size,target);
            RespRecommend recommends = null;
            RelationWords relationWords = null;

            model.put("searchResult", searchResult);
            model.put("totalPages", searchResult.getMeta().getTotalPages());
            model.put("size", searchResult.getMeta().getTotalElements());
            model.put("recommends",recommends);
            model.put("relationWords",relationWords);
        }
        //fixme : query가 empty일 때, 사용자가 공백 검색을 못하게 alert를 띄운다.

        return "search/searchResult";
    }
}
