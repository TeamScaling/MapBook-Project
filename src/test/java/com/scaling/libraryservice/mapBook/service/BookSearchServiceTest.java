package com.scaling.libraryservice.mapBook.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.service.BookSearchService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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
    private BookRepository bookRepository;

    @Autowired
    private BookSearchService bookSearchService;


    @Test
    @DisplayName("기존 검색에서 검색되지 않는 검색어 확인")
    public void searchByTitleTest1() {
        /* given */
        String query = "자바의정석";
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);

        /* when */
        Page<Book> books = bookRepository.findBooksByTitleBmode(query, pageable);

        /* then */
        assertTrue(books.isEmpty());

    }

    @Test
    @DisplayName("검색되지 않는 검색어가 들어왔을 때 동적으로 쿼리 변경이 되는지 확인")
    public void searchByTitleTest2() {
        /* given */
        String query = "자바의정석";
        int page = 1;
        int size = 10;

        /* when */
        RespBooksDto books = bookSearchService.searchBooks(query, page, size,"title");

        /* then */
        //Assertions 로 검색결과가 있는지 확인
        Assertions.assertFalse(books.getDocuments().isEmpty());

        //assertThat으로 반환타입이 List가 맞는지 확인
        assertThat(books.getDocuments()).isInstanceOfAny(List.class);


    }
}