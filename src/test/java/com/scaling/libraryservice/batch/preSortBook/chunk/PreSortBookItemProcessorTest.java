package com.scaling.libraryservice.batch.preSortBook.chunk;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PreSortBookItemProcessorTest {

    @Test
    @DisplayName("중복된 Token을 제거 할 수 있다.")
    public void test() {

        String titleToken = "아프니까 청춘 청춘";
        String author = "김난도";

        String collect = Arrays.stream(String.join(" ", titleToken, author).split(" "))
            .distinct()
            .collect(Collectors.joining(" "));

        long count = Arrays.stream(collect.split(" "))
            .filter(token -> token.equals("청춘"))
            .count();

        assertEquals(count, 1);
    }

    @Test
    @DisplayName("토큰을 합친 작업이 예상한 결과와 일치한다.")
    public void test2() {

        String titleToken = "아프니까 청춘 청춘";
        String author = "김난도";
        String expect = "아프니까 청춘 김난도";

        String collect = Arrays.stream(String.join(" ", titleToken, author).split(" "))
            .distinct()
            .collect(Collectors.joining(" "));

        assertEquals(collect, expect);
    }

}