package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.recommend.repository.RecommendRepository;
import com.scaling.libraryservice.search.domain.TitleQuery;
import com.scaling.libraryservice.search.domain.TitleType;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component @Slf4j
public class EnumBookFinder implements BookFinder<BookDto> {
    private final BookRepository bookRepository;
    private final RecommendRepository recommendRepo;

    @Override
    public Page<BookDto> selectBooks(TitleQuery titleQuery, Pageable pageable){

        return selectBooksEntity(titleQuery,pageable).map(BookDto::new);
    }

    @Override
    public List<BookDto> selectRecommends(TitleQuery titleQuery, int size){

        return selectRecBooksEntity(titleQuery,size).stream().map(BookDto::new).toList();
    }

    /**
     * 검색 대상(target)에 따라 적절한 검색 쿼리를 선택하여 도서를 검색하고, 결과를 반환하는 메서드입니다.
     *
     * @param titleQuery 검색 쿼리를 분석한 결과를 담고 있는 TitleQuery 객체
     * @param pageable   페이지 관련 설정을 담은 Pageable 객체
     * @return 검색 결과를 담은 Page<Book> 객체
     */

    private Page<Book> selectBooksEntity(TitleQuery titleQuery, Pageable pageable) {

        TitleType type = titleQuery.getTitleType();

        switch (type) {

            case KOR_SG, KOR_MT_OVER_TWO -> {
                return bookRepository.findBooksByKorNatural(titleQuery.getKorToken(), pageable);
            }
            case ENG_SG -> {
                return bookRepository.findBooksByEngBool(titleQuery.getEngToken(), pageable);
            }
            case KOR_MT_UNDER_TWO -> {
                return bookRepository.findBooksByKorMtFlexible(titleQuery.getKorToken(), pageable);
            }

            case ENG_MT -> {
                return bookRepository.findBooksByEngMtFlexible(titleQuery.getEngToken(), pageable);
            }

            case KOR_ENG, ENG_KOR_SG -> {
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

    /**
     * 검색어를 분석하여 추천 도서를 조회할 때 사용할 최적의 쿼리를 선택하고, 추천 도서 데이터를 반환합니다.
     *
     * @param titleQuery 검색 쿼리를 분석한 결과를 담고 있는 TitleQuery 객체
     * @param size  추천 도서를 어느 범위까지 보여 줄지에 대한 값
     * @return 선택된 추천 도서 DTO들을 담은 List
     */
    public List<Book> selectRecBooksEntity(TitleQuery titleQuery, int size) {
        TitleType type = titleQuery.getTitleType();

        log.info("Query is [{}] and tokens : [{}]", type.name(), titleQuery);

        switch (type) {

            case KOR_SG, KOR_MT_OVER_TWO -> {
                return recommendRepo.findBooksByKorBoolOrder(titleQuery.getKorToken(), size);

            }

            case KOR_MT_UNDER_TWO -> {
                return recommendRepo.findBooksByKorMtFlexible(titleQuery.getKorToken(), size);
            }

            case ENG_SG -> {
                return recommendRepo.findBooksByEngBoolOrder(titleQuery.getEngToken(), size);
            }
            case ENG_MT -> {
                return recommendRepo.findBooksByEngMtOrderFlexible(titleQuery.getEngToken(), size);
            }
            case KOR_ENG, ENG_KOR_SG -> {
                return recommendRepo.findBooksByEngKorBoolOrder(titleQuery.getEngToken(),
                        titleQuery.getKorToken(), size);
            }

            case ENG_KOR_MT -> {
                return recommendRepo.findBooksByEngKorNaturalOrder(
                    titleQuery.getEngToken(),
                    titleQuery.getKorToken(),
                    size);
            }
            default -> throw new IllegalArgumentException("Invalid title type: " + type);
        }
    }

}
