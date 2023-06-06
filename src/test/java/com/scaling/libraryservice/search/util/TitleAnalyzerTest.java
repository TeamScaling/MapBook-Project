package com.scaling.libraryservice.search.util;

import static com.scaling.libraryservice.search.util.TitleType.ENG_KOR_MT;
import static com.scaling.libraryservice.search.util.TitleType.ENG_KOR_SG;
import static com.scaling.libraryservice.search.util.TitleType.KOR_MT_OVER_TWO;
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

        titleAnalyzer = new TitleAnalyzer(new KomoranTokenizer(new Komoran(DEFAULT_MODEL.FULL)));
    }

    @Test
    @DisplayName("영어+한글 제목을 판별 할 수 있다")
    public void analyze() {
        /* given */

        String title = "Boot Spring Boot! - 한 권으로 정리하는 스프링 부트 A to Z";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */

        assertEquals(ENG_KOR_MT, result.getTitleType());
    }

    @Test
    @DisplayName("영어 제목(명사 2개 이상)을 판별 할 수 있다")
    public void analyze2() {
        /* given */

        String title = "Boot Spring Boot";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */

        assertEquals(TitleType.ENG_MT, result.getTitleType());
    }

    @Test
    @DisplayName("영어 제목(명사 1개)을 판별 할 수 있다")
    public void analyze3() {
        /* given */

        String title = "Boot";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */

        assertEquals(TitleType.ENG_SG, result.getTitleType());
    }

    @Test
    @DisplayName("한글 제목(명사 1개)을 판별 할 수 있다")
    public void analyze4() {
        /* given */

        String title = "스프링";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */

        assertEquals(TitleType.KOR_SG, result.getTitleType());
    }

    @Test
    @DisplayName("한글 제목(명사 2개)을 판별 할 수 있다")
    public void analyze5() {
        /* given */

        String title = "스프링 부트";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */

        assertEquals(TitleType.KOR_MT_TWO, result.getTitleType());
    }

    @Test
    @DisplayName("한글 제목(명사 3개 이상)을 판별 할 수 있다")
    public void analyze6() {
        /* given */

        String title = "스프링 부트 입문";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */

        assertEquals(TitleType.KOR_MT_OVER_TWO, result.getTitleType());
    }

    @Test
    @DisplayName("영+한 제목에서 한글이 더 긴 경우를 판별 할 수 있다")
    public void analyze7() {
        /* given */

        String title = "spring 스프링 입문 초보";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */
        assertEquals(ENG_KOR_MT, result.getTitleType());
    }

    @Test
    @DisplayName("영+한 제목에서 영어가 더 긴 경우를 판별 할 수 있다")
    public void analyze8() {
        /* given */

        String title = "spring javajava 스프링 초보";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */
        System.out.println(result);
        assertEquals(ENG_KOR_MT, result.getTitleType());
    }

    @DisplayName("= 가 들어간 한글 제목을 판별 할 수 있다. ")
    @Test
    public void analyze9() {
        /* given */

        String title = "죽은 왕녀를 위한 파반느 =박민규 장편소설";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */
        System.out.println(result);
        assertEquals(KOR_MT_OVER_TWO, result.getTitleType());
    }

    @DisplayName(": 가 들어간 한글 제목을 판별 할 수 있다. ")
    @Test
    public void analyze10() {
        /* given */

        String title = "죽은 왕녀를 위한 파반느 :박민규 장편소설";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */
        System.out.println(result);
        assertEquals(KOR_MT_OVER_TWO, result.getTitleType());
    }

    @DisplayName("영어가 더 긴 영+한 제목에서 ")
    @Test
    public void analyze11() {
        /* given */

        String title = "springBoot 스프링";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */
        System.out.println(result);
        assertEquals(ENG_KOR_SG, result.getTitleType());
    }



}