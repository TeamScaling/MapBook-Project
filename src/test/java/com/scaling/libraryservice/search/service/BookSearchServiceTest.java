package com.scaling.libraryservice.search.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookSearchServiceTest {

    @Autowired
    private BookSearchService bookSearchService;

    @Test
    public void load(){
    }


    @Test @DisplayName("영어 제목 도서 검색")
    public void only_eng_title(){
        /* given */

        String title = "spring boot";

        /* when */

        var result = bookSearchService.searchBooks(title,1,10,"title");

        /* then */

        System.out.println(result);
    }

    @Test
    public void only_kor_title(){
        /* given */

        String title = "토비 스프링";

        /* when */

        var result = bookSearchService.searchBooks(title,1,10,"title");

        /* then */

        System.out.println(result);
    }

    @Test
    public void eng_kor_title(){
        /* given */

        String title = "퍼펙트 EJB";

        /* when */

        var result = bookSearchService.searchBooks(title,1,10,"title");

        /* then */

        System.out.println(result);
    }

    @Test
    public void eng_kor_title2(){
        /* given */

        String title = "한글 office XP";

        /* when */

        var result = bookSearchService.searchBooks(title,1,10,"title");

        /* then */

        System.out.println(result.getDocuments());
    }

}