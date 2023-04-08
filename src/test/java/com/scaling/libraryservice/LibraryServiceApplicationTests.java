package com.scaling.libraryservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scaling.libraryservice.entity.Book;
import com.scaling.libraryservice.entity.QBook;
import com.scaling.libraryservice.repository.BookQueryRepository;
import com.scaling.libraryservice.repository.BookRepository;
import com.scaling.libraryservice.service.BookSearchService;
import com.scaling.libraryservice.util.Tokenizer;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryServiceApplicationTests {

    @Autowired
    private BookSearchService bookSearchService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BookQueryRepository bookQueryRepo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private Tokenizer tokenizer;


    @Test @DisplayName("스프링 부트 기본 시작을 테스트")
    void contextLoads() {

    }

    @Nested
    class SearchLogic{

        @Test @DisplayName("명사 단위 토큰 검색 방식이 검색 품질이 더 좋을 것이다.")
        public void verify_bookQueryRepo(){
            /* given */
            String title = "자바 정석";
            List<String> tokens = tokenizer.tokenize(title);
            /* when */
            List<Book> books1 = bookRepo.findBooksByTitle(title);
            List<Book> books2 = bookQueryRepo.findBooksByToken(tokens);

            boolean success = books1.size() < books2.size();
            /* then */

            assertTrue(success);

        }

    }


    @Nested
    @DisplayName("다양한 경우에 따른 같은 도서 검색")
    class SearchSimilarBook {

        @Test
        @DisplayName("띄어쓰기 유무에 따른 검색")
        public void search_sameTitle_different_space() {
            /* given */

            String query1 = "토비 스프링";
            String query2 = "토비스프링";

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
        boolean isEqualBook(String query1, String query2) {

            List<String> tokens1 = tokenizer.tokenize(query1);
            List<String> tokens2 = tokenizer.tokenize(query2);

            List<Book> result1 = bookQueryRepo.findBooksByToken(tokens1);
            List<Book> result2 = bookQueryRepo.findBooksByToken(tokens2);

            Book book1 = result1.get(0);
            Book book2 = result2.get(0);

            return Objects.equals(book1.getSeqId(), book2.getSeqId());
        }

    }

    @Test
    void queryDsl_factory_query() {

        /*String query = "select * from books where TITLE_NM like '%자바%' and TITLE_NM like '%정석%'";*/

        List<String> tokens = List.of("자바", "정석");

        JPAQueryFactory qf = new JPAQueryFactory(entityManager);
        JPAQuery<Book> result = qf.selectFrom(QBook.book)
            .where(QBook.book.title.like("%자바%").and(QBook.book.title.like("%정석%")));

        JPAQuery<Book> query1 = qf.selectFrom(QBook.book);

        for (String s : tokens) {

            String str = "%" + s + "%";
            BooleanExpression expression = QBook.book.title.like(str);

            query1.where(expression);
        }

        assertEquals(result.fetch().get(0).getSeqId(), query1.fetch().get(0).getSeqId());

        /*result.fetch().forEach(System.out::println);*/
        /*query1.fetch().forEach(System.out::println);*/
    }



}
