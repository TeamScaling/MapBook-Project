package com.scaling.libraryservice.search.engine.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.search.service.KeywordService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
class ConvertFilterTest {

    @InjectMocks
    ConvertFilter convertFilter;

    @Mock
    KeywordService keywordService;


    @Test
    public void filtering_eng_to_kor(){
        /* given */

        String word = "wkqk wjdtjr";
        String target = "자바 정석";

        when(keywordService.getExistKeywords(List.of("wkqk", "자바", "wjdtjr", "정석")))
            .thenReturn(List.of("자바", "정석"));

        /* when */

        String result = convertFilter.filtering(word);

        /* then */
        assertEquals(result,target);
    }

    @Test
    @DisplayName("변환이 필요 없는 단어는 원형이 유지가 된다. 순서는 상관 없다")
    public void filtering_is_not_required(){
        /* given */

        String word = "spring java";
        String target = "spring";
        String target2 = "java";

        when(keywordService.getExistKeywords(List.of("spring", "java")))
            .thenReturn(List.of("spring", "java"));

        /* when */

        String result = convertFilter.filtering(word);

        /* then */

        assertTrue(result.contains(target));
        assertTrue(result.contains(target2));
    }
}