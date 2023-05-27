package com.scaling.libraryservice.search.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class TitleDividerTest {

    @Test
    public void test_divide(){
        /* given */

        String title = "java 정석";

        /* when */

        var result = TitleDivider.divideKorEng(title);

        /* then */

        assertEquals(String.join("", result.get("eng")), "java");
        assertEquals(String.join("", result.get("kor")), "정석");
    }

}