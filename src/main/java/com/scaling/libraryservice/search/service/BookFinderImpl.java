package com.scaling.libraryservice.search.service;

import static com.scaling.libraryservice.search.util.TitleType.ENG_KOR_MT;
import static com.scaling.libraryservice.search.util.TitleType.ENG_KOR_SG;
import static com.scaling.libraryservice.search.util.TitleType.ENG_MT;
import static com.scaling.libraryservice.search.util.TitleType.ENG_SG;
import static com.scaling.libraryservice.search.util.TitleType.KOR_MT_OVER_TWO;
import static com.scaling.libraryservice.search.util.TitleType.KOR_MT_TWO;
import static com.scaling.libraryservice.search.util.TitleType.KOR_SG;

import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.service.strategy.EngBoolSt;
import com.scaling.libraryservice.search.service.strategy.EngKorBoolSt;
import com.scaling.libraryservice.search.service.strategy.EngKorNaturalSt;
import com.scaling.libraryservice.search.service.strategy.KorBoolSt;
import com.scaling.libraryservice.search.service.strategy.KorNaturalSt;
import com.scaling.libraryservice.search.util.TitleQuery;
import com.scaling.libraryservice.search.util.TitleType;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class BookFinderImpl implements BookFinder<Page<BookDto>, Pageable>{

    private final Map<TitleType,SelectStrategy> strategyMap;

    public BookFinderImpl(BookRepository bookRepository) {
        this.strategyMap = new EnumMap<>(TitleType.class);

        strategyMap.put(KOR_SG, new KorNaturalSt(bookRepository));
        strategyMap.put(KOR_MT_OVER_TWO, new KorNaturalSt(bookRepository));
        strategyMap.put(KOR_MT_TWO, new KorBoolSt(bookRepository));
        strategyMap.put(ENG_SG,new EngBoolSt(bookRepository));
        strategyMap.put(ENG_MT,new EngBoolSt(bookRepository));
        strategyMap.put(ENG_KOR_SG, new EngKorBoolSt(bookRepository));
        strategyMap.put(ENG_KOR_MT, new EngKorNaturalSt(bookRepository));
    }

    /**
     * 검색 대상(target)에 따라 적절한 검색 쿼리를 선택하여 도서를 검색하고, 결과를 반환하는 메서드입니다.
     *
     * @param titleQuery 검색 쿼리를 분석한 결과를 담고 있는 TitleQuery 객체
     * @param pageable   페이지 관련 설정을 담은 Pageable 객체
     * @return 검색 결과를 담은 Page<Book> 객체
     */

    @Override
    public Page<BookDto> findBooks(TitleQuery titleQuery, Pageable pageable) {

        TitleType type = titleQuery.getTitleType();

        SelectStrategy strategy = strategyMap.get(type);

        if(strategy == null){
            throw new IllegalArgumentException("Invalid title type: " + type);
        }

        return strategy.select(titleQuery,pageable).map(BookDto::new);
    }
}
