package com.scaling.libraryservice;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scaling.libraryservice.search.repository.BookRepoQueryDsl;
import com.scaling.libraryservice.search.repository.KeywordQueryDsl;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @PersistenceContext
    EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public BookRepoQueryDsl bookRepoQueryDsl() {
        return new BookRepoQueryDsl(jpaQueryFactory());
    }

    @Bean
    public KeywordQueryDsl keywordQueryDsl() {
        return new KeywordQueryDsl(jpaQueryFactory());
    }

}
