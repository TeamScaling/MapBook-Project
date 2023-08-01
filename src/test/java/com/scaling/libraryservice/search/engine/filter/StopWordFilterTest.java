package com.scaling.libraryservice.search.engine.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StopWordFilterTest {

    StopWordFilter stopWordFilter;

    @BeforeEach
    void setUp() {
        stopWordFilter = new StopWordFilter(null);
    }

    @Test
    public void removeStopWord(){
        /* given */

        String target = "kotlin in   action";
        String expect = "kotlin action";

        /* when */


        /* then */
        String result = stopWordFilter.filtering(target);

        assertEquals(expect,result);
    }




}