package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class BookSearchServiceTest {


    @Autowired
    private BookSearchService bookSearchService;


    @Test
    public void load(){
    }


    @Test
    @DisplayName("책 제목을 이용한 검색")
    void test_searchBooks(){

        String title = "자바의 정석";
        int page = 1;
        int size = 10;
        String target = "title";

        RespBooksDto result = bookSearchService.searchBooks(new BookCacheKey(title,page), size, target);

        Assertions.assertThat(result.getDocuments().get(0).getTitle().contains(title));

    }


    @Test
    @DisplayName("한글 1단어 검색")
    void test_pickSelectQuery_KR(){

        String title = "자바";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookSearchService.pickSelectQuery(title, pageable);

        System.out.println("결과: " + result.getContent().get(0).getTitle().contains(title));

    }

    @Test
    @DisplayName("한글 3단어 이상 검색")
    void test_pickSelectQuery_MT_OVER_TWO(){

        String title = "그리스 로마 신화";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookSearchService.pickSelectQuery(title, pageable);

        Assertions.assertThat(result.getContent().get(0).getTitle().contains(title));

    }

    @Test
    @DisplayName("영어 1단어 검색")
    void test_pickSelectQuery_EN(){

        String title = "java";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookSearchService.pickSelectQuery(title, pageable);

        Assertions.assertThat(result.getContent().get(0).getTitle().contains(title));

    }

    @Test
    @DisplayName("한글 2단어 이하로 검색")
    void test_pickSelectQuery_KOR_MT_UNDER_TWO(){

        String title = "자바의 정석";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookSearchService.pickSelectQuery(title, pageable);

        Assertions.assertThat(result.getContent().get(0).getTitle().contains(title));

    }

    @Test
    @DisplayName("영어 2단어 이상 검색")
    void test_pickSelectQuery_ENG_MT(){

        String title = "spring boot";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookSearchService.pickSelectQuery(title, pageable);

        Assertions.assertThat(result.getContent().get(0).getTitle().contains(title));

    }

    @Test
    @DisplayName("한글 + 영어 검색")
    void test_pickSelectQuery_KOR_ENG_SG(){

        String title = "java 정석";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookSearchService.pickSelectQuery(title, pageable);

        Assertions.assertThat(result.getContent().get(0).getTitle().contains(title));

    }

}