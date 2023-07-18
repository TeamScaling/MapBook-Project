package com.scaling.libraryservice.search.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TitleTrimmerTest {



    @Test
    void splitAddPlus() {

    }

    @Test @DisplayName("제목이 명사 기준 2개 이상 일 때 부제목을 제거 할 수 있다.")
    public void remove_subTitle(){
        /* given */
        String query = "죽은 왕녀를 위한 파반느 : 박민규 장편소설";
        String expect = "죽은 왕녀를 위한 파반느";

        /* when */

        var result =TitleTrimmer.TrimTitleResult(query);

        /* then */
        assertEquals(result,expect);
    }

    @Test @DisplayName("제목에서 불필요한 불용어를 제거 한다.")
    public void remove_subTitle2(){
        /* given */
        String query = "왕녀 박민규 장편소설=@#@";
        String expect = "왕녀 박민규 장편소설";

        /* when */

        var result =TitleTrimmer.TrimTitleResult(query);

        /* then */
        assertEquals(result,expect);
    }

}