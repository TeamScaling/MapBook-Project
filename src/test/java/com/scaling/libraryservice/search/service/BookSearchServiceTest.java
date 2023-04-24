package com.scaling.libraryservice.search.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookSearchServiceTest {

    @Autowired
    private BookSearchService bookSearchService;

    @Test
    public void load(){
        /* given */

        var result = bookSearchService.parsedQuery("window api정복");

        /* when */

        /* then */

        System.out.println(bookSearchService.isEnglish("window api정복"));
    }

}