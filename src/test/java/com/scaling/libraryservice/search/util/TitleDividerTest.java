package com.scaling.libraryservice.search.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.scaling.libraryservice.search.util.TitleDivider.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TitleDividerTest {


    @Test @DisplayName("영어와 한글을 각각 나눌 수 있다.")
    public void test_divide(){
        /* given */

        String title = "java 정석";

        /* when */

        var result = TitleDivider.divideKorEng(title);

        /* then */

        assertTrue(result.get(Language.ENG).contains("java"));
        assertTrue(result.get(Language.KOR).contains("정석"));
    }

    @Test @DisplayName("불용어가 있었도 영어,한글을 분리 할 수 있다.")
    public void test_divide2(){
        /* given */

        String title = "java :@#@% 정석";

        /* when */

        var result = TitleDivider.divideKorEng(title);

        /* then */

        assertTrue(result.get(Language.ENG).contains("java"));
        assertTrue(result.get(Language.KOR).contains("정석"));
    }

    @Test @DisplayName("불용어가 단어 사이에 잘못 위치해도 영어,한글을 분리 할 수 있다.")
    public void test_divide3(){
        /* given */

        String title = "j@ava 정:석";

        /* when */

        var result = TitleDivider.divideKorEng(title);

        /* then */
        assertTrue(result.get(Language.ENG).contains("java"));
        assertTrue(result.get(Language.KOR).contains("정석"));
    }

}