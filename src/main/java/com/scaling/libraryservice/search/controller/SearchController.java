package com.scaling.libraryservice.search.controller;

import com.scaling.libraryservice.logging.logger.SearchLogger;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import com.scaling.libraryservice.search.service.BookSessionService;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
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
    private static final int SESSION_INTERVAL = 3;
    private final SearchLogger searchLogger;

    private final BookSessionService bookSessionService;


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
    public ResponseEntity<RespBooksDto> autocomplete(@RequestParam(value = "query") String query,
        HttpSession session) {

        RespBooksDto books = bookSearchService.autoCompleteSearch(
            new ReqBookDto(query, DEFAULT_PAGE, AUTO_COMPLETE_SIZE), DEFAULT_TIMEOUT, false);

        // 자동 완성을 통해 이미 DB에서 검색한 결과를 session에 잠시 저장하고, 재활용 한다.
        bookSessionService.keepBooksInSession(session,books,SESSION_INTERVAL);

        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/search")
    public ResponseEntity<RespBooksDto> searchBook(
        @RequestParam(value = "query") String query,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        HttpSession session) {

        Optional<RespBooksDto> sessionResult
            = bookSessionService.getBookDtoFromSession(query,session);

        if(sessionResult.isPresent()){
            return ResponseEntity.ok(sessionResult.get());
        }

        RespBooksDto searchResult
            = bookSearchService.searchBooks(
            new ReqBookDto(query, page, size), DEFAULT_TIMEOUT, false);

        searchLogger.sendLogToSlack(searchResult);

        return ResponseEntity.ok(searchResult);
    }

}
