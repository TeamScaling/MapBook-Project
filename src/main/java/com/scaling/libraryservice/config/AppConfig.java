package com.scaling.libraryservice.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {

        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public RestTemplate restTemplate(){

        return new RestTemplate();
    }

}
