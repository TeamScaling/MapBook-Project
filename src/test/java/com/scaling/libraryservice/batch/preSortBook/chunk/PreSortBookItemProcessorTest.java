package com.scaling.libraryservice.batch.preSortBook.chunk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.entity.SortBook;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PreSortBookItemProcessorTest {

    @Autowired
    private PreSortBookItemProcessor preSortBookItemProcessor;

    @Test @DisplayName("title token과 author 데이터를 원하는 데이터로 합칠 수 있다.")
    public void test3(){
        /* given */

        Book book = Book.builder()
            .id(1L)
            .isbn("123")
            .bookImg("test.jpg")
            .title("봄·봄 =Spring, spring ")
            .author("지은이: 김유정 ;옮긴이: 전승희")
            .content("내용")
            .loanCnt(200)
            .publishDate("2014")
            .build();

        String expect = "spring 김유정 전승희";

        /* when */

        SortBook sortBook = preSortBookItemProcessor.process(book);

        /* then */
        assertEquals(sortBook.getTitleToken(),expect);
    }

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