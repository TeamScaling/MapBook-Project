package com.scaling.libraryservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.scaling.libraryservice.entity.Book;
import com.scaling.libraryservice.repository.BookQueryRepository;
import com.scaling.libraryservice.repository.BookRepository;
import com.scaling.libraryservice.service.BookSearchService;
import com.scaling.libraryservice.util.Tokenizer;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryServiceApplicationTests {

    @Autowired
    private BookRepository bookRepo;
    @Autowired
    private BookSearchService bookService;

    @Test @DisplayName("스프링 부트 기본 시작을 테스트")
    void contextLoads() {

    }

    /*@Test @DisplayName("가장 단순한 검색 방법")
    public void findBook_byTitle_basic(){
        *//* given *//*

        String title = "스프링";

        *//* when *//*

        List<Book> books = bookService.searchBook(title);

        //consider : 스프링이란 책을 찾았을 때, DB에 아무 것도 없다면?
        Book target = books.get(0);

        boolean success = target.getTitle().contains(title);

        *//* then *//*
        assertTrue(success);
    }*/


    @Test @DisplayName("query를 책 제목과 책 소개 검색을 통해 정확도 상승")
    public void findBook_by_TitleAndContent(){
        /* given */

        String target = "스프링";
        String fakeBook = "예배를 위한 찬송가 반주 & 연주곡집 (스프링)";

        /* when */
        List<Book> books = bookRepo.findBooksByTitleAndContent(target);
        /* then */
        System.out.println(books);
        books.forEach(System.out::println);

    }


    @Test @DisplayName("해당 책을 융통성 있게 찾을 수 있어야 하는데 못 찾음")
    public void findBook_with_Title_fail(){
        /* given */

        String title = "토비의 스프링";

        /* when */
        List<Book> books = bookRepo.findBooksByTitleAndContent(title);

        /* then */

        assertEquals(0,books.size());

        books.forEach(System.out::println);
    }

    @Nested @DisplayName("다양한 경우에 따른 같은 도서 검색")
    class SearchSimilarBook{

        @Autowired
        private BookQueryRepository bookQueryRepo;

        @Autowired
        private Tokenizer tokenizer;
        @Test @DisplayName("띄어쓰기 유무에 따른 검색")
        public void search_sameTitle_different_space(){
            /* given */

            String query1 = "토비 스프링";
            String query2 = "스프링 토비";

            /* when */
            boolean isEqualBook = isEqualBook(query1,query2);

            /* then */
            Assertions.assertTrue(isEqualBook);
        }

        @Test @DisplayName("조사 유무에 따른 검색")
        void search_sameTitle_different_Josa(){

        /*consider : 해당 테스트는 쿼리 생성을 보는 테스트이므로
                 tokenizer도 mock으로 만들어서 단위를 분리 해야함.*/
            /* given */

            String query1 = "토비 스프링";
            String query2 = "스프링 토비";

            /* when */
            boolean isEqualBook = isEqualBook(query1,query2);

            /* then */
            Assertions.assertTrue(isEqualBook);
        }

        @Test @DisplayName("명사 위치에 따른 검색")
        void search_sameTitle_different_position(){
            /* given */

            String query1 = "토비 스프링";
            String query2 = "스프링 토비";

            /* when */
            boolean isEqualBook = isEqualBook(query1,query2);

            /* then */
            Assertions.assertTrue(isEqualBook);
        }


        boolean isEqualBook(String query1,String query2){

            List<String> tokens1 = tokenizer.tokenize(query1);
            List<String> tokens2 = tokenizer.tokenize(query2);

            /* when */
            List<Book> result1 = bookQueryRepo.findBooksByToken(tokens1);
            List<Book> result2 = bookQueryRepo.findBooksByToken(tokens2);

            Book book1 = result1.get(0);
            Book book2 = result2.get(0);

            System.out.println(result1);
            System.out.println(result2);


            return Objects.equals(book1.getSeqId(), book2.getSeqId());
        }

    }



}
