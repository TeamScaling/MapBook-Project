package com.scaling.libraryservice.search.util.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.scaling.libraryservice.search.service.KeywordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    public void filtering(){
        /* given */

        String word = "wkqk wjdtjr";
        String target = "자바 정석";

        /* when */

        String result = convertFilter.filtering(word);

        /* then */

        assertEquals(result,target);

    }

    @Test
    @DisplayName("변환이 필요 없는 단어는 원형이 유지가 된다. 순서는 상관 없다")
    public void filtering2(){
        /* given */

        String word = "spring java";
        String target = "spring";
        String target2 = "java";

        /* when */

        String result = convertFilter.filtering(word);

        /* then */

        assertTrue(result.contains(target));
        assertTrue(result.contains(target2));

    }
}