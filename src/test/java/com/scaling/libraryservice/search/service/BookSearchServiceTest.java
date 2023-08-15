package com.scaling.libraryservice.search.service;

import static com.scaling.libraryservice.search.engine.TitleType.TOKEN_TWO_OR_MORE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.commons.async.AsyncExecutor;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.BookDto.BookDtoBuilder;
import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.RespBooksDtoFactory;
import com.scaling.libraryservice.search.engine.TitleAnalyzer;
import com.scaling.libraryservice.search.engine.TitleQuery;
import com.scaling.libraryservice.search.engine.TitleQuery.TitleQueryBuilder;
import com.scaling.libraryservice.search.repository.BookRepoQueryDsl;
import com.scaling.libraryservice.search.repository.BookRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookSearchServiceTest {

    @InjectMocks
    private BookSearchService bookSearchService;

    @Mock
    private TitleAnalyzer titleAnalyzer;

    @Mock
    private BookRepoQueryDsl bookRepoQueryDsl;

    @Mock
    private AsyncExecutor<Page<BookDto>, ReqBookDto> asyncExecutor;

    @Test @DisplayName("사용자 검색어에 대한 분석 결과가 있을 때, 적절한 검색 결과를 반환 할 수 있다.")
    void searchBooks() {
        /* given */
        String query = "java 정석";

        ReqBookDto reqBookDto = new ReqBookDto(query, 1, 10);

        TitleQuery titleQuery = new TitleQueryBuilder()
            .titleType(TOKEN_TWO_OR_MORE)
            .etcToken("java")
            .nnToken("정석")
            .build();

        BookDto bookDto = BookDto.builder().title("java의 정석").isbn("43252334").build();
        Page<BookDto> page = new PageImpl<>(List.of(bookDto));

        when(titleAnalyzer.analyze(query, true)).thenReturn(titleQuery);
        when(asyncExecutor.execute(any(), any(), anyInt(), anyBoolean()))
            .thenReturn(page);

        /* when */

        RespBooksDto result = bookSearchService.searchBooks(reqBookDto, 3, false);

        /* then */
        assertFalse(result.isEmptyResult());
    }

    @Test @DisplayName("사용자 검색어에 대한 분석 결과가 없을 때, 빈 검색 결과를 반환 할 수 있다.")
    void searchBooks2() {
        /* given */
        String query = "";

        ReqBookDto reqBookDto = new ReqBookDto(query, 1, 10);

        TitleQuery titleQuery = new TitleQueryBuilder()
            .titleType(TOKEN_TWO_OR_MORE)
            .etcToken("")
            .nnToken("")
            .userQuery(query)
            .build();

        when(titleAnalyzer.analyze(query, true)).thenReturn(titleQuery);

        /* when */

        RespBooksDto result = bookSearchService.searchBooks(reqBookDto, 3, false);

        /* then */

        assertTrue(result.isEmptyResult());
    }

    @Test @DisplayName("ISBN으로 도서를 검색 할 수 있다.")
    void can_search_Books_by_Isbn() {
        /* given */
        String query = "9788950906030";
        ReqBookDto reqBookDto = new ReqBookDto(query, 1, 10);

        BookDto bookDto = BookDto.builder()
            .isbn(query)
            .title("7년의 밤 :정유정 장편소설 ")
            .build();

        when(bookRepoQueryDsl.findBooksByIsbn(query)).thenReturn(bookDto);

        /* when */

        var result = bookSearchService.searchBooks(reqBookDto, 3, false);

        /* then */
        assertNotNull(result);
    }

    @Test
    public void isIsbn() {
        /* given */

        String query = "9788973374113";

        /* when */
        boolean result = bookSearchService.isIsbnQuery(query);
        /* then */

        assertTrue(result);
    }

    @Test
    public void is_Not_Isbn() {
        /* given */

        String query = "자바의 정석";

        /* when */
        boolean result = bookSearchService.isIsbnQuery(query);
        /* then */

        assertFalse(result);
    }


}