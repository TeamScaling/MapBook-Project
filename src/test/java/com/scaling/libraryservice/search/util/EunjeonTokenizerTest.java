package com.scaling.libraryservice.search.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void tokenize() {
        /* given */

        String target = "자바의 정석";

        /* when */
        var result = tokenizer.tokenize(target);

        /* then */

        assertTrue(result.get(Language.KOR).stream().anyMatch(s -> s.equals("자바")));
        assertTrue(result.get(Language.KOR).stream().anyMatch(s -> s.equals("정석")));
    }

    @Test
    @DisplayName("영어+한글 제목에서 한글 명사만 추출 할 수 있다.")
    void tokenize2() {
        /* given */

        String target = "java의 정석";

        /* when */
        var result = tokenizer.tokenize(target);

        /* then */

        assertTrue(result.get(Language.KOR).stream().anyMatch(s -> s.equals("정석")));
    }



}