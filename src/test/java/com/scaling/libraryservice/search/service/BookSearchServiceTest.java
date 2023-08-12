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
import com.scaling.libraryservice.search.engine.TitleAnalyzer;
import com.scaling.libraryservice.search.engine.TitleQuery;
import com.scaling.libraryservice.search.engine.TitleQuery.TitleQueryBuilder;
import com.scaling.libraryservice.search.repository.BookRepoQueryDsl;
import com.scaling.libraryservice.search.repository.BookRepository;
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
    private BookRepoQueryDsl bookRepoQueryDsl;

    @Mock
    private AsyncExecutor<Page<BookDto>, ReqBookDto> asyncExecutor;

    @Test
    void searchBooks() {
        /* given */
        String query = "java 정석";

        ReqBookDto reqBookDto = new ReqBookDto("java 정석", 1, 10);

        TitleQuery titleQuery = new TitleQueryBuilder()
            .titleType(TOKEN_TWO_OR_MORE)
            .etcToken("java")
            .nnToken("정석")
            .build();

        when(titleAnalyzer.analyze(query, true)).thenReturn(titleQuery);
        when(asyncExecutor.execute(any(), any(), anyInt(), anyBoolean())).thenReturn(Page.empty());

        /* when */

        var result = bookSearchService.searchBooks(reqBookDto, 3, false);

        /* then */
        assertNotNull(result);
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