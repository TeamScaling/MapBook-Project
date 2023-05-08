package com.scaling.libraryservice.search.controller;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.mapBook.dto.LoanItemDto;
import com.scaling.libraryservice.mapBook.dto.TestingBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j @RequiredArgsConstructor
public class SearchRestController {

    private final BookSearchService bookSearchService;

    @GetMapping("/books/search/test")
    public ResponseEntity<RespBooksDto> restSearchBook(@RequestBody TestingBookDto testingBookDto){

        var result = bookSearchService.searchBooks(testingBookDto.getBookName(),1,10,"title");

        if(result.getDocuments().isEmpty()){
            log.info("[Not Found]This book is Not Found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

}
