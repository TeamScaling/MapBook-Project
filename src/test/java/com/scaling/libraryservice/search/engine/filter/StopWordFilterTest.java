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

    @Test
    public void removeStopWord2(){
        /* given */

        String title =
            "do it 자바스크립트 in 제이쿼리 the 입문";

        String expect = "do 자바스크립트 제이쿼리 입문";
        /* when */

        var result = stopWordFilter.filtering(title);
        /* then */

        assertEquals(expect,result);
    }




}