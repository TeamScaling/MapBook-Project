package com.scaling.libraryservice.search.repository;

import static com.scaling.libraryservice.search.entity.QBook.book;
import static com.scaling.libraryservice.search.util.SearchMode.BooleanMode;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.util.TitleQuery;
import com.scaling.libraryservice.search.util.TitleTrimmer;
import com.scaling.libraryservice.search.util.TitleType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookRepoQueryDsl implements BookRepository {

    private final JPAQueryFactory factory;

    private final static int LIMIT_CNT = 30;

    private final static double SCORE_OF_MATCH = 0.0;

    @Override
    public Page<BookDto> findBooks(TitleQuery titleQuery, Pageable pageable) {

        System.out.println(titleQuery.getEngKorTokens()+":"+titleQuery.getTitleType());

        JPAQuery<Book> books = factory
            .selectFrom(book)
            .where(
                getTemplate(
                    titleQuery.getTitleType(), titleQuery.getEngKorTokens()).gt(SCORE_OF_MATCH))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        long totalSize;

        if (titleQuery.getTitleType() == TitleType.TOKEN_ONE) {
            totalSize = LIMIT_CNT;
        }else{
            totalSize = countQuery(titleQuery).fetchOne();
        }

        long finalTotalSize = totalSize;

        return PageableExecutionUtils.getPage(
            books.fetch().stream().map(BookDto::new).toList(),
            pageable, () -> finalTotalSize);
    }

    public Page<Book> findAllAndSort(Pageable pageable){

        JPAQuery<Book> books = factory
            .selectFrom(book)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(book.loanCnt.desc());

        return PageableExecutionUtils.getPage(
            books.fetch(),
            pageable, () -> countQueryAll().fetchOne());
    }


    private JPAQuery<Long> countQuery(TitleQuery titleQuery) {

        return factory
            .select(book.count())
            .from(book)
            .where(
                getTemplate(
                    titleQuery.getTitleType()
                    , titleQuery.getEngKorTokens()).gt(SCORE_OF_MATCH)
            );
    }

    private JPAQuery<Long> countQueryAll() {

        return factory
            .select(book.count())
            .from(book);
    }


    NumberTemplate<Double> getTemplate(TitleType type, String name) {

        String function;

        if (type.getMode() == BooleanMode) {
            function = "function('BooleanMatch',{0},{1})";
            name = TitleTrimmer.splitAddPlus(name);
        } else {
            function = "function('NaturalMatch',{0},{1})";
        }


        return Expressions.numberTemplate(Double.class,
            function, book.titleToken, name);
    }
}
