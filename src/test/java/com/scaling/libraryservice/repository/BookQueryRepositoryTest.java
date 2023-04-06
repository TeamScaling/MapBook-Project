package com.scaling.libraryservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scaling.libraryservice.entity.Book;
import com.scaling.libraryservice.entity.QBook;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookQueryRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void queryDsl_factory_query() {

        /*String query = "select * from books where TITLE_NM like '%자바%' and TITLE_NM like '%정석%'";*/

        List<String> tokens = List.of("자바","정석");

        JPAQueryFactory qf = new JPAQueryFactory(entityManager);
        JPAQuery<Book> result = qf.selectFrom(QBook.book)
            .where(QBook.book.title.like("%자바%").and(QBook.book.title.like("%정석%")));

        JPAQuery<Book> query1 = qf.selectFrom(QBook.book);

        for(String s : tokens){

            String str = "%"+s+"%";
            BooleanExpression expression = QBook.book.title.like(str);

            query1.where(expression);
        }

        assertEquals(result.fetch().get(0).getSeqId(), query1.fetch().get(0).getSeqId());

        /*result.fetch().forEach(System.out::println);*/
        /*query1.fetch().forEach(System.out::println);*/
    }



}