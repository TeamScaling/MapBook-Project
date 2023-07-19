package com.scaling.libraryservice.search.util;

import static com.scaling.libraryservice.search.util.TitleType.TOKEN_ONE;
import static com.scaling.libraryservice.search.util.TitleType.TOKEN_TWO_OR_MORE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TitleAnalyzerTest {

    private TitleAnalyzer titleAnalyzer;

    @BeforeEach
    public void setUp() {

        titleAnalyzer = new TitleAnalyzer(new EunjeonTokenizer());
    }

    @Test
    @DisplayName("영어+한글 제목을 판별 할 수 있다")
    public void analyze() {
        /* given */

        String title = "Boot Spring Boot! - 한 권으로 정리하는 스프링 부트 A to Z";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */
        System.out.println(result);
        assertEquals(TOKEN_TWO_OR_MORE, result.getTitleType());
    }

    @Test
    @DisplayName("한글 제목(명사 1개)을 판별 할 수 있다")
    public void analyze4() {
        /* given */

        String title = "스프링";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */

        assertEquals(TOKEN_ONE, result.getTitleType());
    }




}