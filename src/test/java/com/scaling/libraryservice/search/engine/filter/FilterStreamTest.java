package com.scaling.libraryservice.search.engine.filter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FilterStreamTest {

    FilterStream filterStream;

    @BeforeEach
    void setUp() {

        filterStream = new FilterStream(new SimpleFilter(new StopWordFilter(null)));
    }

    @Test
    public void doFiltering(){
        /* given */

        String title = "지은이: 주호민";
        String expect = "곰돌이 co 강경효";

        /* when */

        var arr = title.split(";")[0];

        System.out.println(arr);

        String result = filterStream.doFiltering(title,true);
        System.out.println(result);

        /* then */
//
//        assertEquals(result,expect);
    }

}