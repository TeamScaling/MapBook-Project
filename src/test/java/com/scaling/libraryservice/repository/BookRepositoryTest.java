package com.scaling.libraryservice.repository;

import com.scaling.libraryservice.entity.Book;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Nested
    class SearchTitle {

        @Test
        @DisplayName("검색 단어 위치와 상관 없이 검색이 되는가")
        public void findBooksByTitle() {
            /* given */
            String title1 = "자바 정석";
            String title2 = "정석 자바";


            /* when */
            List<Book> books1 = bookRepository.findBooksByTitleNormal(title1);
            List<Book> books2 = bookRepository.findBooksByTitleNormal(title2);

            Book book1 = books1
                .stream()
                .filter(book -> book.getSeqId().equals(3123163))
                .findFirst()
                .get();

            Book book2 = books1
                .stream()
                .filter(book -> book.getSeqId().equals(3123163))
                .findFirst()
                .get();
            //.toString()을 사용해도됨

            /* then */
            //9788994492032
            //3123163
            Assertions.assertEquals(book1, book2);
        }

        @Test
        @DisplayName("띄어쓰기 유무에 따른 검색")
        public void search_sameTitle_different_space() {
            /* given */

            String query1 = "토비 스프링";
            String query2 = "토비스프링";

            System.out.println(splitTarget(query2));

            /* when */
            boolean isEqualBook = isEqualBook(query1, query2);

            /* then */
            Assertions.assertTrue(isEqualBook);
        }

        @Test
        @DisplayName("조사 유무에 따른 검색")
        void search_sameTitle_different_Josa() {

        /*consider : 해당 테스트는 쿼리 생성을 보는 테스트이므로
                 tokenizer도 mock으로 만들어서 단위를 분리 해야함.*/
            /* given */

            String query1 = "토비 스프링";
            String query2 = "스프링 토비";

            /* when */
            boolean isEqualBook = isEqualBook(query1, query2);

            /* then */
            Assertions.assertTrue(isEqualBook);
        }

        @Test
        @DisplayName("명사 위치에 따른 검색")
        void search_sameTitle_different_position() {
            /* given */

            String query1 = "토비 스프링";
            String query2 = "스프링 토비";

            /* when */
            boolean isEqualBook = isEqualBook(query1, query2);

            /* then */
            Assertions.assertTrue(isEqualBook);
        }

        // 테스트에 필요한 공통 메서드
        boolean isEqualBook(String title1, String title2) {

            List<Book> result1 = bookRepository.findBooksByTitleNormal(splitTarget(title1));
            List<Book> result2 = bookRepository.findBooksByTitleNormal(splitTarget(title2));

            Book book1 = result1.get(0);
            Book book2 = result2.get(0);

            return Objects.equals(book1.getSeqId(), book2.getSeqId());
        }

        private String splitTarget(String target) {
            return Arrays.stream(target.split(" "))
                .map(name -> "+" + name)
                .collect(Collectors.joining(" "));
        }
    }


}
