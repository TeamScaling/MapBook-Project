package com.scaling.libraryservice.search.util;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TitleAnalyzerTest {

    private TitleAnalyzer titleAnalyzer;

    @BeforeEach
    public void init(){

        titleAnalyzer = new TitleAnalyzer(new KomoranTokenizer(new Komoran(DEFAULT_MODEL.FULL)));
    }

    @Test
    public void load(){
        /* given */

        String title = "Boot Spring Boot! - 한 권으로 정리하는 스프링 부트 A to Z";

        /* when */

        var result = titleAnalyzer.analyze(title);

        /* then */

        System.out.println(result);
    }

    @Test
    public void is_duplicate_instance(){
        /* given */

        String title = "Boot Spring Boot! - 한 권으로 정리하는 스프링 부트 A to Z";
        String title2 = "통계적 품질관리";

        /* when */

        var result = titleAnalyzer.analyze(title);
        var reulst2 = titleAnalyzer.analyze(title2);

        /* then */

        System.out.println(result);
        System.out.println(reulst2);


    }

    @Test
    public void token_kor(){
        /* given */

        KomoranTokenizer tokenizer = new KomoranTokenizer(new Komoran(DEFAULT_MODEL.FULL));

        /* when */

        String title = "내가 고양이를 데리고\n"
            + "노는 것일까, 고양이가\n"
            + "나를 데리고 노는 것일\n"
            + "까? :내가 나를 쓴 최초\n"
            + "의 철학자 몽테뉴의 12\n"
            + "가지 고민들";

        var result = tokenizer.tokenize(title);

        /* then */

        System.out.println(result);
    }

    @Test
    public void test_remove_keyWord(){
        /* given */

        String query = "28 : 정유정 장편소설 이야기 한국사";
        String removeQuery = "28 : 정유정";

        /* when */
        var result = titleAnalyzer.removeKeyword(query);

        /* then */

        Assertions.assertEquals(result,removeQuery);
    }

    @Test
    public void test_load(){
        /* given */
        var result = "아몬드손원평 장편소설".contains("아몬드:손원평 장편소설");
        /* when */
        System.out.println(result);
        /* then */
    }

}