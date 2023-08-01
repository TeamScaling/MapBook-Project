package com.scaling.libraryservice.search.engine;

import static com.scaling.libraryservice.search.engine.Token.NN_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
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

        assertTrue(result.get(NN_TOKEN).stream().anyMatch(s -> s.equals("자바")));
        assertTrue(result.get(NN_TOKEN).stream().anyMatch(s -> s.equals("정석")));
    }

    @Test
    @DisplayName("영어+한글 제목에서 한글 명사만 추출 할 수 있다.")
    void tokenize2() {
        /* given */

        String target = "java의 정석";

        /* when */
        var result = tokenizer.tokenize(target);

        /* then */

        assertTrue(result.get(NN_TOKEN).stream().anyMatch(s -> s.equals("정석")));
    }

    @Test
    @DisplayName("영어+한글 제목에서 한글 명사만 추출 할 수 있다.")
    void tokenize3() {
        /* given */

        String target = "확장 가능하고 유지보수가 쉬운";

        /* when */
        var result = tokenizer.tokenize(target);

        /* then */

        System.out.println(result);
    }

    @Test
    @DisplayName("영어+한글 제목에서 한글 명사만 추출 할 수 있다.")
    void tokenize4() {
        /* given */

        String target = "인간적인";
        String nnToken = "인간";

        /* when */
        String result = tokenizer.getEtcTokens(List.of(nnToken),target);

        /* then */
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("영어+한글 제목에서 한글 명사만 추출 할 수 있다.")
    void tokenize5() {
        /* given */

        String target = "왜 칸트인가";
        String nnToken = "칸트";
        String expect = "왜";

        /* when */
        String result = tokenizer.getEtcTokens(List.of(nnToken),target);

        /* then */
        System.out.println(result);
        assertEquals(expect,result);
    }




}