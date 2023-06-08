package com.scaling.libraryservice.recommend.service;

import com.scaling.libraryservice.recommend.repository.RecommendRepository;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.service.BookFinder;
import com.scaling.libraryservice.search.util.TitleQuery;
import com.scaling.libraryservice.search.util.TitleType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


/**
 * 사용자의 추천 도서 목록을 찾는 클래스입니다.
 *
 * <p>이 클래스는 {@link BookFinder} 인터페이스를 구현합니다. {@link BookFinder#findBooks} 메소드를 통해
 * 추천 도서를 검색하고, 그 결과를 {@link BookDto} 형태의 리스트로 반환합니다.</p>
 *
 * <p>추천 도서 정보는 {@link RecommendRepository}를 통해 조회됩니다.</p>
 *
 */
@RequiredArgsConstructor
@Slf4j @Component
public class EnumRecommendFinder implements BookFinder<List<BookDto>, Integer> {

    private final RecommendRepository recommendRepo;

    /**
     * 주어진 쿼리와 사용자 ID를 이용하여 추천 도서를 찾아 {@link BookDto} 형태의 리스트로 반환합니다.
     *
     * @param titleQuery 검색 쿼리 정보를 담은 {@link TitleQuery} 객체.
     * @param target 사용자 ID
     * @return 찾아진 추천 도서 정보를 담은 {@link BookDto} 객체의 리스트
     */
    @Override
    public List<BookDto> findBooks(TitleQuery titleQuery, Integer target) {
        return selectRecBooksEntity(titleQuery,target).stream().map(BookDto::new).toList();
    }

    /**
     * 검색어를 분석하여 추천 도서를 조회할 때 사용할 최적의 쿼리를 선택하고, 추천 도서 데이터를 반환합니다.
     *
     * @param titleQuery 검색 쿼리를 분석한 결과를 담고 있는 TitleQuery 객체
     * @param size  추천 도서를 어느 범위까지 보여 줄지에 대한 값
     * @return 선택된 추천 도서를 담은 List
     */
    private List<Book> selectRecBooksEntity(@NonNull TitleQuery titleQuery, int size) {
        TitleType type = titleQuery.getTitleType();

        log.info("Query is [{}] and tokens : [{}]", type.name(), titleQuery);

        switch (type) {

            case KOR_SG, KOR_MT_OVER_TWO -> {
                return recommendRepo.findBooksByKorBoolOrder(titleQuery.getKorToken(), size);
            }

            case KOR_MT_TWO -> {
                return recommendRepo.findBooksByKorMtFlexible(titleQuery.getKorToken(), size);
            }

            case ENG_SG -> {
                return recommendRepo.findBooksByEngBoolOrder(titleQuery.getEngToken(), size);
            }

            case ENG_MT -> {
                return recommendRepo.findBooksByEngMtOrderFlexible(titleQuery.getEngToken(), size);
            }
            case ENG_KOR_SG -> {
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
