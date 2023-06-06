package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.recommend.repository.RecommendRepository;
import com.scaling.libraryservice.search.util.TitleQuery;
import com.scaling.libraryservice.search.util.TitleType;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component @Slf4j
public class EnumBookFinder implements BookFinder<Page<BookDto>,Pageable> {
    private final BookRepository bookRepository;
    @Override
    public Page<BookDto> findBooks(TitleQuery titleQuery, Pageable pageable){
        return selectBooksEntity(titleQuery,pageable).map(BookDto::new);
    }

    /**
     * 검색 대상(target)에 따라 적절한 검색 쿼리를 선택하여 도서를 검색하고, 결과를 반환하는 메서드입니다.
     *
     * @param titleQuery 검색 쿼리를 분석한 결과를 담고 있는 TitleQuery 객체
     * @param pageable   페이지 관련 설정을 담은 Pageable 객체
     * @return 검색 결과를 담은 Page<Book> 객체
     */

    private Page<Book> selectBooksEntity(@NonNull TitleQuery titleQuery, Pageable pageable) {

        TitleType type = titleQuery.getTitleType();

        switch (type) {

            case KOR_SG, KOR_MT_OVER_TWO -> {
                return bookRepository.findBooksByKorNatural(titleQuery.getKorToken(), pageable);
            }

            case KOR_MT_TWO -> {
                return bookRepository.findBooksByKorMtFlexible(titleQuery.getKorToken(), pageable);
            }

            case ENG_SG -> {
                return bookRepository.findBooksByEngBool(titleQuery.getEngToken(), pageable);
            }

            case ENG_MT -> {
                return bookRepository.findBooksByEngMtFlexible(titleQuery.getEngToken(), pageable);
            }

            case ENG_KOR_SG -> {
                return bookRepository.findBooksByEngKorBool(titleQuery.getEngToken(),
                    titleQuery.getKorToken(), pageable);
            }

            case ENG_KOR_MT -> {
                return bookRepository.findBooksByEngKorNatural(
                    titleQuery.getEngToken(),
                    titleQuery.getKorToken(),
                    pageable);
            }

            default -> throw new IllegalArgumentException("Invalid title type: " + type);
        }
    }



}
