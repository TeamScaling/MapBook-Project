package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.search.util.EunjeonTokenizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LearningTest {

    @Autowired
    EunjeonTokenizer tokenizer;

    @Test
    public void test(){
        /* given */

        String str = "자바 성능을 결정짓는 코딩 습관과 튜닝 이야기";

        var result = tokenizer.tokenize(str);

        /* when */

        System.out.println(result);

        /* then */
    }

}
