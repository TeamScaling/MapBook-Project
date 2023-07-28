package com.scaling.libraryservice.search.controller;

import com.scaling.libraryservice.logging.logger.SearchLogger;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    private final BookSearchService bookSearchService;
    private static final int AUTO_COMPLETE_SIZE = 6;
    private static final int DEFAULT_TIMEOUT = 3;
    private static final int DEFAULT_PAGE = 1;
    private final SearchLogger searchLogger;



    /**
     * 메인 홈페이지를 반환하는 메서드입니다.
     *
     * @return 홈페이지의 View 이름
     */
    @GetMapping("/")
    public String home() {
        return "search/searchResult";
    }

    @PostMapping(value = "/books/autocomplete")
    public ResponseEntity<List<BookDto>> autocomplete(@RequestParam(value = "query") String query) {

        List<BookDto> books = bookSearchService.autoCompleteSearch(
            new ReqBookDto(query, DEFAULT_PAGE, AUTO_COMPLETE_SIZE), DEFAULT_TIMEOUT, false);

        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/search")
    public ResponseEntity<RespBooksDto> searchBook(
        @RequestParam(value = "query") String query,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "10") int size) {

        RespBooksDto searchResult
            = bookSearchService.searchBooks(
            new ReqBookDto(query, page, size), DEFAULT_TIMEOUT, true);

        searchLogger.sendLogToSlack(searchResult);

        return ResponseEntity.ok(searchResult);
    }

}
