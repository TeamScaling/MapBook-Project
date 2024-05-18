package com.scaling.libraryservice.search.repository;

import static com.scaling.libraryservice.search.entity.QKeyword.keyword1;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scaling.libraryservice.search.entity.Keyword;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class KeywordQueryDsl {

    private final JPAQueryFactory factory;

    @Transactional(readOnly = true)
    public List<Keyword> getKeywords(String... words) {
        return words.length == 0
            ? Collections.emptyList()
            : factory.selectFrom(keyword1)
                .where(keyword1.keyword.in(words))
                .fetch();
    }

}
