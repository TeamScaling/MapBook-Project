package com.scaling.libraryservice.search.engine.filter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleFilterTest {

    SimpleFilter simpleFilter;

    @BeforeEach
    void setUp() {
        simpleFilter = new SimpleFilter(null);
    }

    @Test
    public void test() {
        /* given */

        String title = "왜 칸트인가 ";
        String expect = "왜 칸트인가";

        /* when */
        String result = simpleFilter.filtering(title);

        /* then */

        assertEquals(result,expect);
    }

}