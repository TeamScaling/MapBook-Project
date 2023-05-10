package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class BookSearchServiceTest {


    @Autowired
    private BookSearchService bookSearchService;

    @Autowired
    private CustomCacheManager<RespBooksDto> cacheManager;

    @Test
    public void load(){
    }


    @Test @DisplayName("영어 제목 도서 검색")
    public void only_eng_title(){
        /* given */

        String title = "spring boot";

        /* when */

        var result = bookSearchService.searchBooks(title,1,10,"title");

        /* then */

        System.out.println(result);
    }

    @Test
    public void only_kor_title(){
        /* given */

        String title = "토비 스프링";

        /* when */

        var result = bookSearchService.searchBooks(title,1,10,"title");

        /* then */

        System.out.println(result);
    }

    @Test
    public void eng_kor_title(){
        /* given */

        String title = "퍼펙트 EJB";

        /* when */

        var result = bookSearchService.searchBooks(title,1,10,"title");

        /* then */

        System.out.println(result);
    }

    @Test
    public void eng_kor_title2(){
        /* given */

        String title = "한글 office XP";

        /* when */

        var result = bookSearchService.searchBooks(title,1,10,"title");

        /* then */

        System.out.println(result.getDocuments());
    }

//    @Test
//    @DisplayName("캐싱 테스트")
//    public void testCustomCacheable() {
//        // Given
//        String query = "자바의 정석";
//        int page = 1;
//        int size = 10;
//        String target = "title";
//        BookCacheKey bookCacheKey;
//
//        //  When
//        //  첫 번째 호출
//        RespBooksDto result1 = bookSearchService.searchBooks(query, page, size, target);
//
//        // 캐시된 데이터 가져오기
//        RespBooksDto cachedResult = customCacheManager.isContainItem(bookCacheKey, BookCacheKey).get(query, RespBooksDto.class);
//
//        //  두 번째 호출
//        RespBooksDto result2 = bookSearchService.searchBooks(query, page, size, target);
//
//        // Then
//        Assertions.assertThat(result2).isNotEqualTo(result1);
//        System.out.println(result1);
//        System.out.println(result2);
//
//    }

    @Test
    @DisplayName("책 제목을 이용한 검색")
    void test_searchBooks(){

        String title = "자바의 정석";
        int page = 1;
        int size = 10;
        String target = "title";

        RespBooksDto result = bookSearchService.searchBooks(title, page, size, target);

//        Assertions.assertThat(result.getDocuments().get(0).getTitle().contains(title));
        System.out.println(result.getDocuments().get(0).getTitle());

    }

//    @Test
//    void test_asyncSearchBook(){
//
//        String title = "자바";
//        int page = 1;
//        int size = 10;
//        Pageable pageable = PageRequest.of(0, 10);
//
////        RespBooksDto respBooksDto = null;
//
//        bookSearchService.asyncSearchBook(title, page, size);
//
//        BookCacheKey bookCacheKey = new BookCacheKey(title, page);
//
//        Page<Book> fetchedBooks = bookSearchService.pickSelectQuery(title, pageable);
//        if (fetchedBooks != null && !fetchedBooks.isEmpty()) {
//            RespBooksDto respBooksDto = new RespBooksDto(
//                new MetaDto(fetchedBooks.getTotalPages(),
//                    fetchedBooks.getTotalElements(), page, size),
//                fetchedBooks.stream().map(BookDto::new).toList());
//
////            boolean result1 = customCacheManager.isContainItem(respBooksDto.getClass(), bookCacheKey);
//
//            boolean result2 = cacheManager.isContainItem(bookSearchService.getClass(), bookCacheKey);
//
//            System.out.println(result2);
////            Assertions.assertThat(result1).isNotEqualTo(result2);
//        }
//    }

    @Test
    @DisplayName("한글 1단어 검색")
    void test_pickSelectQuery_KR(){

        String title = "자바";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookSearchService.pickSelectQuery(title, pageable);

        System.out.println("결과: " + result.getContent().get(0).getTitle().contains(title));

    }

    @Test
    @DisplayName("한글 3단어 이상 검색")
    void test_pickSelectQuery_MT_OVER_TWO(){

        String title = "그리스 로마 신화";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookSearchService.pickSelectQuery(title, pageable);

        Assertions.assertThat(result.getContent().get(0).getTitle().contains(title));

    }

    @Test
    @DisplayName("영어 1단어 검색")
    void test_pickSelectQuery_EN(){

        String title = "java";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookSearchService.pickSelectQuery(title, pageable);

        Assertions.assertThat(result.getContent().get(0).getTitle().contains(title));

    }

    @Test
    @DisplayName("한글 2단어 이하로 검색")
    void test_pickSelectQuery_KOR_MT_UNDER_TWO(){

        String title = "자바의 정석";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookSearchService.pickSelectQuery(title, pageable);

        Assertions.assertThat(result.getContent().get(0).getTitle().contains(title));

    }

    @Test
    @DisplayName("영어 2단어 이상 검색")
    void test_pickSelectQuery_ENG_MT(){

        String title = "spring boot";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookSearchService.pickSelectQuery(title, pageable);

        Assertions.assertThat(result.getContent().get(0).getTitle().contains(title));

    }

    @Test
    @DisplayName("한글 + 영어 검색")
    void test_pickSelectQuery_KOR_ENG_SG(){

        String title = "java 정석";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookSearchService.pickSelectQuery(title, pageable);

        Assertions.assertThat(result.getContent().get(0).getTitle().contains(title));

    }

//    @Test
//    @DisplayName("한글 2글자 + 영어 2글자 이상 검색")
//    void test_pickSelectQuery_ENG_KOR_MT(){
//
//        String title = "springboot 스프링 부트";
//        Pageable pageable = PageRequest.of(0, 10);
//
//        Page<Book> result = bookSearchService.pickSelectQuery(title, pageable);
//
////        Assertions.assertThat(result.getContent().get(0).getTitle().contains(title));
//        System.out.println((result.getContent()));
//
//    }


}