package com.scaling.libraryservice.search.engine;

import org.junit.jupiter.api.BeforeEach;

class EunjeonTokenizerTest {

    private EunjeonTokenizer tokenizer;

    @BeforeEach
    void setUp() {
        tokenizer = new EunjeonTokenizer();
    }

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
//        assertTrue(result.get(Language.KOR_N).stream().anyMatch(s -> s.equals("자바")));
//        assertTrue(result.get(Language.KOR_N).stream().anyMatch(s -> s.equals("정석")));
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
//        assertTrue(result.get(Language.KOR_N).stream().anyMatch(s -> s.equals("정석")));
//    }

//    @Test
//    @DisplayName("영어+한글 제목에서 한글 명사만 추출 할 수 있다.")
//    void tokenize3() {
//        /* given */
//
//        String target = "확장 가능하고 유지보수가 쉬운";
//
//        /* when */
//        var result = tokenizer.tokenize(target);
//
//        /* then */
//
//        System.out.println(result);
//    }



}