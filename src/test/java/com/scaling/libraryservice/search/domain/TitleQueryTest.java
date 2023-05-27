package com.scaling.libraryservice.search.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TitleQueryTest {

    @Test
    public void test_build(){
        /* given */

        var result = TitleQuery.builder().titleType(TitleType.KOR_MT_OVER_TWO).korToken("hi").build();

        /* when */

        /* then */

        System.out.println(result);
    }

}