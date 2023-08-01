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

    @Test
    public void removeSpecialChar() {
        /* given */

        String title = "it!";
        String expect = "it";

        /* when */
        String result = simpleFilter.removeSpecialChar(title);

        /* then */
        assertEquals(result,expect);
    }

    @Test
    public void removeSpecialChar2() {
        /* given */

        String title = "i@@t! @@hi";
        String expect = "it hi";

        /* when */
        String result = simpleFilter.removeSpecialChar(title);
        System.out.println(result);
        /* then */
        assertEquals(result,expect);
    }

}