package com.scaling.libraryservice.search.service;

import static com.scaling.libraryservice.search.entity.QBook.book;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
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


//    @Test
//    public void finder() {
//
//        TitleQuery query = analyzer.analyze("java 정석");
//
//        var result = queryDslFinder.searchBook(query, query.getKorToken(), PageRequest.of(0, 10));
//
//        result.getContent().stream().forEach(System.out::println);
//    }

}