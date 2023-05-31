package com.scaling.libraryservice.search.service;

import static com.scaling.libraryservice.search.util.TitleType.ENG_KOR_MT;
import static com.scaling.libraryservice.search.util.TitleType.ENG_KOR_SG;
import static com.scaling.libraryservice.search.util.TitleType.ENG_MT;
import static com.scaling.libraryservice.search.util.TitleType.ENG_SG;
import static com.scaling.libraryservice.search.util.TitleType.KOR_ENG;
import static com.scaling.libraryservice.search.util.TitleType.KOR_MT_OVER_TWO;
import static com.scaling.libraryservice.search.util.TitleType.KOR_MT_UNDER_TWO;
import static com.scaling.libraryservice.search.util.TitleType.KOR_SG;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.util.TitleQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class EnumBookFinderTest {

    @InjectMocks
    private EnumBookFinder enumBookFinder;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("KOR_SG 타입이 들어오면 DB를 통해 Book을 반환 할 수 있다")
    void selectBooks() {
        /* given */
        TitleQuery titleQuery = TitleQuery.builder().titleType(KOR_SG).korToken("자바").build();
        Pageable pageable = PageRequest.of(1, 10);

        when(bookRepository.findBooksByKorNatural(titleQuery.getKorToken(), pageable)).thenReturn(
            Page.empty());
        /* when */
        var result = enumBookFinder.selectBooks(titleQuery, pageable);

        /* then */
        assertNotNull(result);
    }

    @Test
    @DisplayName("KOR_MT_OVER_TWO 타입이 들어오면 DB를 통해 Book을 반환 할 수 있다")
    void selectBooks2() {
        /* given */
        TitleQuery titleQuery = TitleQuery.builder().titleType(KOR_MT_OVER_TWO).korToken("자바 정석 입문")
            .build();
        Pageable pageable = PageRequest.of(1, 10);

        when(bookRepository.findBooksByKorNatural(titleQuery.getKorToken(), pageable)).thenReturn(
            Page.empty());
        /* when */
        var result = enumBookFinder.selectBooks(titleQuery, pageable);

        /* then */
        assertNotNull(result);
    }

    @Test
    @DisplayName("ENG_SG 타입이 들어오면 DB를 통해 Book을 반환 할 수 있다")
    void selectBooks3() {
        /* given */
        TitleQuery titleQuery = TitleQuery.builder().titleType(ENG_SG).engToken("java").build();
        Pageable pageable = PageRequest.of(1, 10);

        when(bookRepository.findBooksByEngBool(titleQuery.getEngToken(), pageable)).thenReturn(
            Page.empty());
        /* when */
        var result = enumBookFinder.selectBooks(titleQuery, pageable);

        /* then */
        assertNotNull(result);
    }

    @Test
    @DisplayName("KOR_MT_UNDER_TWO 타입이 들어오면 DB를 통해 Book을 반환 할 수 있다")
    void selectBooks4() {
        /* given */
        TitleQuery titleQuery = TitleQuery.builder().titleType(KOR_MT_UNDER_TWO).korToken("자바 정석")
            .build();
        Pageable pageable = PageRequest.of(1, 10);

        when(
            bookRepository.findBooksByKorMtFlexible(titleQuery.getKorToken(), pageable)).thenReturn(
            Page.empty());
        /* when */
        var result = enumBookFinder.selectBooks(titleQuery, pageable);

        /* then */
        assertNotNull(result);
    }

    @Test
    @DisplayName("ENG_MT 타입이 들어오면 DB를 통해 Book을 반환 할 수 있다")
    void selectBooks5() {
        /* given */
        TitleQuery titleQuery = TitleQuery.builder().titleType(ENG_MT)
            .engToken("java spring").build();
        Pageable pageable = PageRequest.of(1, 10);

        when(
            bookRepository.findBooksByEngMtFlexible(titleQuery.getEngToken(), pageable)).thenReturn(
            Page.empty());
        /* when */
        var result = enumBookFinder.selectBooks(titleQuery, pageable);

        /* then */
        assertNotNull(result);
    }

    @Test
    @DisplayName("KOR_ENG, ENG_KOR_SG 타입이 들어오면 DB를 통해 Book을 반환 할 수 있다")
    void selectBooks6() {
        /* given */
        TitleQuery titleQuery = TitleQuery.builder().titleType(KOR_ENG).engToken("java").korToken("스프링 자바")
            .build();
        TitleQuery titleQuery2 = TitleQuery.builder().titleType(ENG_KOR_SG).engToken("java").korToken("스프링")
            .build();

        Pageable pageable = PageRequest.of(1, 10);

        when(bookRepository.findBooksByEngKorBool(titleQuery.getEngToken(), titleQuery.getKorToken(),
                pageable)).thenReturn(Page.empty());
        when(bookRepository.findBooksByEngKorBool(titleQuery2.getEngToken(), titleQuery2.getKorToken(),
            pageable)).thenReturn(Page.empty());
        /* when */
        var result = enumBookFinder.selectBooks(titleQuery, pageable);
        var result2 = enumBookFinder.selectBooks(titleQuery2, pageable);

        /* then */
        assertNotNull(result);
        assertNotNull(result2);
    }

    @Test
    @DisplayName("ENG_KOR_MT 타입이 들어오면 DB를 통해 Book을 반환 할 수 있다")
    void selectBooks7() {
        /* given */
        TitleQuery titleQuery = TitleQuery.builder().titleType(ENG_KOR_MT)
            .engToken("java spring").korToken("자바 스프링").build();
        Pageable pageable = PageRequest.of(1, 10);

        when(
            bookRepository.findBooksByEngKorNatural(
            titleQuery.getEngToken(),
            titleQuery.getKorToken(),
            pageable)).thenReturn(
            Page.empty());
        /* when */
        var result = enumBookFinder.selectBooks(titleQuery, pageable);

        /* then */
        assertNotNull(result);
    }



    @Test
    void selectRecommends() {
        /* given */

        /* when */

        /* then */
    }

    @Test
    void selectRecBooksEntity() {
        /* given */

        /* when */

        /* then */
    }
}