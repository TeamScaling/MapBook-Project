package com.scaling.libraryservice.recommend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.recommend.dto.ReqRecommendDto;
import com.scaling.libraryservice.recommend.service.RecommendService;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.service.BookFinder;
import com.scaling.libraryservice.search.util.TitleAnalyzer;
import com.scaling.libraryservice.search.util.TitleQuery;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookRecommendServiceTest {

    @InjectMocks
    private RecommendService recommendService;

    @Mock
    private TitleAnalyzer titleAnalyzer;

    @Mock
    private BookFinder querySelector;

    @Test
    @DisplayName("추천 도서 검색")
    void test_recommend_book() {
        /* given */
        String title = "고양이 탐구생활 (개정판) : 고양이이에 관한 잡다한 지식 사전 - 고양이에 관한 잡다한 지식 사전";

        TitleQuery titleQuery = mock(TitleQuery.class);

        BookDto bookDto1 = BookDto.builder().title("고양이 탐구생활").build();
        BookDto bookDto2 = BookDto.builder().title("고양이이에 관한 잡다한 지식 사전").build();

        List<BookDto> books = List.of(bookDto1,bookDto2);

        when(titleAnalyzer.analyze(title)).thenReturn(titleQuery);
        when(querySelector.findRecommends(titleQuery,5)).thenReturn(books);

        /* when */
        List<String> recommendBook = recommendService.getRecommendBook(new ReqRecommendDto(title));

        /* then */

        assertEquals(2,recommendBook.size());
    }

}
