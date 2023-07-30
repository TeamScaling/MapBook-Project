package com.scaling.libraryservice.search.engine.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StopWordFilterTest {

    @Test
    public void removeStopWord(){
        /* given */

        String target = "kotlin in   action";
        String expect = "kotlin action";

        /* when */

        StopWordFilter stopWordFilter = new StopWordFilter(null);

        /* then */
        String result = stopWordFilter.filtering(target);

        assertEquals(expect,result);
    }



}