package com.scaling.libraryservice.search.engine;

import static com.scaling.libraryservice.search.engine.Token.ETC_TOKEN;
import static com.scaling.libraryservice.search.engine.Token.NN_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EunjeonTokenizerTest {

    private EunjeonTokenizer tokenizer;

    @BeforeEach
    void setUp() {
        tokenizer = new EunjeonTokenizer();
    }

    @Test
    @DisplayName("한글 제목에서 명사를 추출 할 수 있다.")
    void tokenize_nnToken() {
        /* given */

        String target = "자바의 정석";

        /* when */
        Map<Token,List<String>> result = tokenizer.tokenize(target);

        /* then */

        assertTrue(result.get(NN_TOKEN).stream().anyMatch(s -> s.equals("자바")));
        assertTrue(result.get(NN_TOKEN).stream().anyMatch(s -> s.equals("정석")));
    }

    @Test
    @DisplayName("영어+한글 제목에서 명사를 추출 할 수 있다.")
    void tokenize_korEng_nnToken() {
        /* given */

        String target = "java의 정석";

        /* when */
        Map<Token,List<String>> result = tokenizer.tokenize(target);

        /* then */

        assertTrue(result.get(NN_TOKEN).stream().anyMatch(s -> s.equals("정석")));
        assertTrue(result.get(NN_TOKEN).stream().anyMatch(s -> s.equals("java")));
    }

    @Test
    void tokenize_etcToken() {
        /* given */

        String target = "아프니까 청춘이다";

        /* when */
        Map<Token,List<String>> result = tokenizer.tokenize(target);

        /* then */

        assertTrue(result.get(ETC_TOKEN).stream().anyMatch(s -> s.equals("아프니까")));
        assertFalse(result.get(ETC_TOKEN).stream().anyMatch(s -> s.equals("청춘")));
    }





}