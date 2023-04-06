package com.scaling.libraryservice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scaling.libraryservice.entity.Book;
import com.scaling.libraryservice.entity.QBook;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 검색 + 페이징
    public Page<Book> findBooksByToken(List<String> tokens, Pageable pageable) {
        JPAQuery<Book> query = createBookQueryByToken(tokens);

        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());

        List<Book> books = query.fetch();
        long totalCount = books.size();

        return new PageImpl<>(books, pageable, totalCount);
    }

    // 검색용 쿼리 생성
    private JPAQuery<Book> createBookQueryByToken(List<String> tokens) {

        JPAQuery<Book> query = jpaQueryFactory.selectFrom(QBook.book);

        for (String s : tokens) {
            String str = "%" + s + "%";
            BooleanExpression expression = QBook.book.title.like(str);
            query.where(expression);
        }

        return query;
    }


    // 기존 검색 (페이징 X)
    // 검색하고자 하는 query를 구문 분석하여 나뉘어진 토큰의 갯수에 맞게 동적으로 query를 생성하고자 함.
    public List<Book> findBooksByToken(List<String> tokens) {

        return createBookQueryByToken(tokens).fetch();
    }


}



