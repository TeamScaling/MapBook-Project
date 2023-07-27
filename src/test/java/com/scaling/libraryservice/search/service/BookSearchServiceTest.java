package com.scaling.libraryservice.search.service;

import static com.scaling.libraryservice.search.util.TitleType.TOKEN_TWO_OR_MORE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.commons.async.AsyncExecutor;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.ReqBookDto;
import com.scaling.libraryservice.search.util.TitleAnalyzer;
import com.scaling.libraryservice.search.util.TitleQuery;
import com.scaling.libraryservice.search.util.TitleQuery.TitleQueryBuilder;
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
    private AsyncExecutor<Page<BookDto>, ReqBookDto> asyncExecutor;

    @Test
    void searchBooks() {
        /* given */
        String query = "java 정석";

        ReqBookDto reqBookDto = new ReqBookDto("java 정석", 1, 10);

        TitleQuery titleQuery = new TitleQueryBuilder().titleType(TOKEN_TWO_OR_MORE)
            .etcToken("java").nnToken("정석").build();

        when(titleAnalyzer.analyze(query,true)).thenReturn(titleQuery);
        when(asyncExecutor.execute(any(), any(), anyInt(),anyBoolean())).thenReturn(Page.empty());

        /* when */

        var result = bookSearchService.searchBooks(reqBookDto, 3,false);

        /* then */
        assertNotNull(result);
    }


}