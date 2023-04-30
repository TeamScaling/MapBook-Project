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

}