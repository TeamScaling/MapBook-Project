package com.scaling.libraryservice.search.service;

import static com.scaling.libraryservice.search.entity.QBook.book;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scaling.libraryservice.search.entity.QBook;
import com.scaling.libraryservice.search.repository.BookRepoQueryDsl;
import com.scaling.libraryservice.search.util.TitleAnalyzer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookRepoQueryDslTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    TitleAnalyzer analyzer;

    @Autowired
    BookRepoQueryDsl bookRepoQueryDsl;


    @Test
    public void finder() {

        JPAQueryFactory factory = new JPAQueryFactory(em);

        String function = "function('BooleanMatch',{0},{1})";
        String function2 = "function('NaturalMatch',{0},{1})";

        QBook subBook = new QBook("subBook");

        var expression= Expressions.numberTemplate(Double.class,
            function, book.titleToken, "+청춘");

        var expression2= Expressions.numberTemplate(Double.class,
            function2, book.title, "아프니까 이다");

        var result = factory.selectFrom(book)
            .where(
                expression.gt(0),
                expression2.gt(0)
            ).fetch();

        System.out.println(result);

    }

}