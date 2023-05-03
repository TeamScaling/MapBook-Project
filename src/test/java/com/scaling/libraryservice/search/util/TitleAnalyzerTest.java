package com.scaling.libraryservice.search.util;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TitleAnalyzerTest {

    private TitleAnalyzer titleAnalyzer;

    @BeforeEach
    public void init(){

        titleAnalyzer = new TitleAnalyzer(new TitleTokenizer(new Komoran(DEFAULT_MODEL.FULL)));
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

        TitleTokenizer tokenizer = new TitleTokenizer(new Komoran(DEFAULT_MODEL.FULL));

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

}