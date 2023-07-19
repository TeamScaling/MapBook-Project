package com.scaling.libraryservice.search.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KomoranTokenizerTest {

//    private EunjeonTokenizer tokenizer;
//
//    @BeforeEach
//    void setUp() {
//        tokenizer = new EunjeonTokenizer();
//    }
//
//    @Test
//    @DisplayName("한글 제목에서 명사를 추출 할 수 있다.")
//    void tokenize() {
//        /* given */
//
//        String target = "자바의 정석";
//
//        /* when */
//        var result = tokenizer.tokenize(target);
//
//        /* then */
//
//        assertTrue(result.stream().anyMatch(s -> s.equals("자바")));
//        assertTrue(result.stream().anyMatch(s -> s.equals("정석")));
//    }
//
//    @Test
//    @DisplayName("영어+한글 제목에서 한글 명사만 추출 할 수 있다.")
//    void tokenize2() {
//        /* given */
//
//        String target = "java의 정석";
//
//        /* when */
//        var result = tokenizer.tokenize(target);
//
//        /* then */
//
//        assertTrue(result.stream().anyMatch(s -> s.equals("정석")));
//    }
//
//    @Test
//    @DisplayName("2글자 이상의 명사만 추출 한다.")
//    void tokenize3() {
//        /* given */
//
//        String target = "꿈을 찾아주는 레인보우 메시지";
//
//        /* when */
//        var result = tokenizer.tokenize(target);
//
//        /* then */
//        assertFalse(result.stream().anyMatch(s -> s.equals("꿈")));
//        assertTrue(result.stream().anyMatch(s -> s.equals("레인보우")));
//        assertTrue(result.stream().anyMatch(s -> s.equals("메시지")));
//    }
//
//    @Test
//    @DisplayName("2글자 이상의 명사만 추출 한다.")
//    void tokenize4() {
//        /* given */
//
//        String target = "아프니까 청춘";
//
//        /* when */
//        var result = tokenizer.tokenize(target);
//
//        /* then */
//        System.out.println(result);
//    }


}