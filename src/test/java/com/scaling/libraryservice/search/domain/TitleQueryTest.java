package com.scaling.libraryservice.search.domain;

import com.scaling.libraryservice.search.util.TitleQuery;
import com.scaling.libraryservice.search.util.TitleType;
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