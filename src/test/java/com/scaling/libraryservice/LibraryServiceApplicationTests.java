package com.scaling.libraryservice;

import com.scaling.libraryservice.entity.Book;
import com.scaling.libraryservice.repository.BookQueryRepository;
import com.scaling.libraryservice.repository.BookRepository;
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


    @Test @DisplayName("스프링 부트 기본 시작을 테스트")
    void contextLoads() {

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

        // 테스트에 필요한 공통 메서드
        boolean isEqualBook(String query1,String query2){

            List<String> tokens1 = tokenizer.tokenize(query1);
            List<String> tokens2 = tokenizer.tokenize(query2);

            List<Book> result1 = bookQueryRepo.findBooksByToken(tokens1);
            List<Book> result2 = bookQueryRepo.findBooksByToken(tokens2);

            Book book1 = result1.get(0);
            Book book2 = result2.get(0);

            return Objects.equals(book1.getSeqId(), book2.getSeqId());
        }

    }



}
