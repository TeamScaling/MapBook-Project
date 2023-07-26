package com.scaling.libraryservice.search.util.filter;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.search.service.KeywordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConvertFilterTest {

    ConvertFilter convertFilter;

    @Autowired
    KeywordService keywordService;

    @BeforeEach
    void setUp() {
        convertFilter = new ConvertFilter(null,keywordService);
    }

    @Test
    public void test(){
        /* given */

        String word = "자바";

        var result = convertFilter.filtering(word);

        /* when */

        System.out.println(result);

        /* then */
    }

}