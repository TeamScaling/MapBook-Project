package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.recommend.service.RecommendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RecommendServiceTest {

    @Autowired
    private RecommendService recommendService;

    @Test
    public void load_mt_eng(){
        /* given */
        String title = "html5 css3";
        /* when */

        var result= recommendService.pickSelectQuery(title,5);
        /* then */


        result.forEach(System.out::println);
    }

    @Test
    public void load_mt_kor(){
        /* given */
        String title = "닥터 지바고";
        /* when */

        var result= recommendService.pickSelectQuery(title,5);
        /* then */


        result.forEach(System.out::println);
    }

}