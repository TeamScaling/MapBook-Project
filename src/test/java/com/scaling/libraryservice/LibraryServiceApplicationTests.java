package com.scaling.libraryservice;

import com.scaling.libraryservice.service.BookSearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryServiceApplicationTests {

    @Autowired
    private BookSearchService bookSearchService;


    @Test @DisplayName("스프링 부트 기본 시작을 테스트")
    void contextLoads() {

    }



}
