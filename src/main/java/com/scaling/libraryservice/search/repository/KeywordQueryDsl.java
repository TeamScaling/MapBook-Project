package com.scaling.libraryservice.search.repository;

import static com.scaling.libraryservice.search.entity.QKeyword.keyword1;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scaling.libraryservice.search.entity.Keyword;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KeywordQueryDsl {

    private final JPAQueryFactory factory;

    public List<Keyword> getKeywords(String... words) {

        if(words.length == 0){
            return Collections.emptyList();
        }

        BooleanBuilder builder = new BooleanBuilder();

        Arrays.stream(words).forEach(
            word -> builder.or(keyword1.keyword.eq(word)));

        return factory
            .selectFrom(keyword1)
            .where(builder).fetch();
    }

}
