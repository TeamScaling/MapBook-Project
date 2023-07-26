package com.scaling.libraryservice.search.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KeywordQueryDslTest {

    @Autowired
    KeywordQueryDsl keywordQueryDsl;

    @Test
    public void getKeywords() {

        var result = keywordQueryDsl.getKeywords("자바", "정석", "spring", "wkqk");

        assertTrue(result.stream().anyMatch(word -> word.getKeyword().equals("자바")));
        assertTrue(result.stream().anyMatch(word -> word.getKeyword().equals("정석")));
        assertTrue(result.stream().anyMatch(word -> word.getKeyword().equals("spring")));
        assertFalse(result.stream().anyMatch(word -> word.getKeyword().equals("wkqk")));
    }

}