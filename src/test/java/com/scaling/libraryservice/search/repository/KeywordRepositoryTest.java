package com.scaling.libraryservice.search.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KeywordRepositoryTest {

    @Autowired
    KeywordRepository keywordRepo;

    @Test
    public void existsKeyword(){
        /* given */

        String query = "사람";

        /* when */

        boolean result = keywordRepo.existsKeywordByKeyword(query);


        /* then */

        assertTrue(result);
    }

    @Test
    public void notExistsKeyword(){
        /* given */

        String query = "wkqk";

        /* when */

        boolean result = keywordRepo.existsKeywordByKeyword(query);


        /* then */

        assertFalse(result);
    }

}