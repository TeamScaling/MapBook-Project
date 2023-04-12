package com.scaling.libraryservice.service;

import java.util.Arrays;
import java.util.List;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.scaling.libraryservice.util.Tokenizer;
import org.springframework.beans.factory.annotation.Autowired;

class TokenizerTest {

    private Tokenizer tokenizer;

    @BeforeEach
    public void setUp(){

        tokenizer = new Tokenizer(new Komoran(DEFAULT_MODEL.FULL));
    }

    @Test
    public void title_Tokens_accuracy(){
        /* given */
        String target = "아프니까 청춘";
        /* when */

        List<String> result = tokenizer.tokenize(target);

        List<String> result2 = Arrays.stream(target.split(" ")).toList();
    
        /* then */

        System.out.println(result);
        System.out.println(result2);
    }

}