package com.scaling.libraryservice.search.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TitleAnalyzerTest {

    @Autowired
    TitleAnalyzer analyzer;

    @Test
    public void test(){
        /* given */

        var result = analyzer.analyze("왜 칸트인가",true);

        /* when */

        System.out.println(result);

        /* then */
    }

}