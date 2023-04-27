package com.scaling.libraryservice.search.controller;

import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import com.scaling.libraryservice.search.service.RelatedContents;
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

    private final RelatedContents relatedContents;

    @GetMapping("/")
    public String home() {
        return "search/home";
    }

    @GetMapping("/books/search")
    public String SearchBook(@RequestParam(value = "query", required = false) String query,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "target", defaultValue = "title") String target, ModelMap model) {

        if (!query.isEmpty()) {
            RespBooksDto result1 = searchService.searchBooks2(query, page, size,target);
            RespBooksDto result2 = relatedContents.relatedContents(result1);

            // searchService.searchBooks2() 메소드에서 리턴된 객체를 searchResult로 대체
            RespBooksDto searchResult = new RespBooksDto(result1.getMeta(), result1.getDocuments(), result2.getRelatedBooks(), result2.getTokenDto());


            model.put("searchResult", searchResult);
            model.put("totalPages", searchResult.getMeta().getTotalPages());
            model.put("size", searchResult.getMeta().getTotalElements());
        }
        //fixme : query가 empty일 때, 사용자가 공백 검색을 못하게 alert를 띄운다.

        return "search/searchResult";
    }
}
