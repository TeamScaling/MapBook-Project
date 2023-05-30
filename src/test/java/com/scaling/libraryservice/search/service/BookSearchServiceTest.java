package com.scaling.libraryservice.search.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.search.cacheKey.ReqBookDto;
import com.scaling.libraryservice.search.domain.TitleQuery;
import com.scaling.libraryservice.search.domain.TitleType;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.util.TitleAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

@ExtendWith(MockitoExtension.class)
class BookSearchServiceTest {

    @InjectMocks
    private BookSearchService bookSearchService;

    @Mock
    private TitleAnalyzer titleAnalyzer;

    @Mock
    private BookRepository bookRepo;


    @Test
    public void load(){
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void searchBooks() {
        /* given */
        String query = "java 정석";

        ReqBookDto reqBookDto = new ReqBookDto("java 정석",1,10);

        TitleQuery titleQuery = TitleQuery.builder().titleType(TitleType.ENG_KOR_SG).engToken("java").korToken("정석").build();

        when(titleAnalyzer.analyze(query)).thenReturn(titleQuery);
        when(bookRepo.findBooksByEngKorBool(any(),any(),any())).thenReturn(Page.empty());

        /* when */

        var result = bookSearchService.searchBooks(reqBookDto,3);

        /* then */

        assertNotNull(result);
    }

    @Test
    void searchBooks_async() {
        /* given */
        String query = "java 정석";

        ReqBookDto reqBookDto = new ReqBookDto("java 정석",1,10);
        TitleQuery titleQuery = TitleQuery.builder().titleType(TitleType.ENG_KOR_SG).engToken("java").korToken("정석").build();


        when(titleAnalyzer.analyze(query)).thenReturn(titleQuery);

        when(bookRepo.findBooksByEngKorBool(any(),any(),any())).thenReturn(Page.empty())
            .thenAnswer(invocation -> {
                Thread.sleep(4000); // 3 seconds delay
                return Page.empty();
            });

        /* when */

        var result = bookSearchService.searchBooks(reqBookDto,1);

        /* then */

        assertNotNull(result);
    }

    @Test
    void asyncSearchBook() {
    }

    @Test
    void pickSelectQuery() {
    }



    @Test
    @DisplayName("책 제목을 이용한 검색")
    void test_searchBooks(){

        String title = "자바의 정석";
        int page = 1;
        int size = 10;
        String target = "title";

        RespBooksDto result = bookSearchService.searchBooks(new ReqBookDto(title,page,size),3);

        assertTrue(result.getDocuments().get(0).getTitle().contains(title));

    }


//    @Test
//    @DisplayName("한글 1단어 검색")
//    void test_pickSelectQuery_KR(){
//
//        String title = "자바";
//        Pageable pageable = PageRequest.of(0, 10);
//
//        var titleQuery= titleAnalyzer.analyze(title);
//
//        Page<Book> result = bookSearchService.pickSelectQuery(titleQuery, pageable);
//
//        System.out.println("결과: " + result.getContent().get(0).getTitle().contains(title));
//
//        assertTrue(result.getContent().get(0).getTitle().contains(title));
//
//    }
//
//    @Test
//    @DisplayName("한글 3단어 이상 검색")
//    void test_pickSelectQuery_MT_OVER_TWO(){
//
//        String title = "그리스 로마 신화";
//        Pageable pageable = PageRequest.of(0, 10);
//
//        var titleQuery= titleAnalyzer.analyze(title);
//
//        Page<Book> result = bookSearchService.pickSelectQuery(titleQuery, pageable);
//
//        assertTrue(result.getContent().get(0).getTitle().contains(title));
//
//    }
//
//    @Test
//    @DisplayName("영어 1단어 검색")
//    void test_pickSelectQuery_EN(){
//
//        String title = "java";
//        Pageable pageable = PageRequest.of(0, 10);
//
//        var titleQuery= titleAnalyzer.analyze(title);
//
//        Page<Book> result = bookSearchService.pickSelectQuery(titleQuery, pageable);
//
//        assertTrue(result.getContent().get(0).getTitle().contains(title));
//
//    }
//
//    @Test
//    @DisplayName("한글 2단어 이하로 검색")
//    void test_pickSelectQuery_KOR_MT_UNDER_TWO(){
//
//        String title = "자바의 정석";
//        Pageable pageable = PageRequest.of(0, 10);
//
//        var titleQuery= titleAnalyzer.analyze(title);
//
//        Page<Book> result = bookSearchService.pickSelectQuery(titleQuery, pageable);
//
//        assertTrue(result.getContent().get(0).getTitle().contains(title));
//
//    }
//
//    @Test
//    @DisplayName("영어 2단어 이상 검색")
//    void test_pickSelectQuery_ENG_MT(){
//
//        String title = "spring boot";
//        Pageable pageable = PageRequest.of(0, 10);
//
//        var titleQuery= titleAnalyzer.analyze(title);
//
//        Page<Book> result = bookSearchService.pickSelectQuery(titleQuery, pageable);
//
//        assertTrue(result.getContent().get(0).getTitle().contains(title));
//
//    }
//
//    @Test
//    @DisplayName("한글 + 영어 검색")
//    void test_pickSelectQuery_KOR_ENG_SG(){
//
//        String title = "java 정석";
//        Pageable pageable = PageRequest.of(0, 10);
//
//        var titleQuery= titleAnalyzer.analyze(title);
//
//        Page<Book> result = bookSearchService.pickSelectQuery(titleQuery, pageable);
//
//        assertTrue(result.getContent().get(0).getTitle().contains(title));
//
//    }


}