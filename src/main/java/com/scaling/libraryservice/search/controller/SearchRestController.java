package com.scaling.libraryservice.search.controller;

import com.scaling.libraryservice.mapBook.dto.TestingBookDto;
import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SearchRestController {

    private final BookSearchService bookSearchService;

    @GetMapping("/books/search/test")
    public ResponseEntity<RespBooksDto> restSearchBook(@NonNull @RequestBody TestingBookDto testingBookDto) {

        RespBooksDto result = bookSearchService.searchBooks(
            new ReqBookDto(testingBookDto.getBookName(), 1,10),3);

        if (result.getDocuments().isEmpty()) {
            log.info("[Not Found]This book is Not Found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

}
