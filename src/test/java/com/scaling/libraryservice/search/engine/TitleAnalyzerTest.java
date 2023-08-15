package com.scaling.libraryservice.search.engine;

import static com.scaling.libraryservice.search.engine.TitleType.TOKEN_ALL_ETC;
import static com.scaling.libraryservice.search.engine.TitleType.TOKEN_COMPLEX;
import static com.scaling.libraryservice.search.engine.TitleType.TOKEN_TWO_OR_MORE;
import static com.scaling.libraryservice.search.engine.Token.ETC_TOKEN;
import static com.scaling.libraryservice.search.engine.Token.NN_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.search.engine.filter.FilterStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TitleAnalyzerTest {

    @InjectMocks
    TitleAnalyzer analyzer;

    @Mock
    FilterStream filterStream;

    @Mock
    EunjeonTokenizer tokenizer;

    @Test
    public void TitleQuery_is_TOKEN_ONE() {
        /* given */

        String title = "크리스마스";

        Map<Token, List<String>> tokenMap = getTokenMap(List.of("크리스마스"),Collections.emptyList());

        when(filterStream.doFiltering(title, true)).thenReturn(title);
        when(tokenizer.tokenize(title)).thenReturn(tokenMap);

        /* when */

        TitleQuery sut = analyzer.analyze(title, true);

        /* then */
        assertEquals(sut.getTitleType(), TitleType.TOKEN_ONE);
    }

    @Test
    public void TitleQuery_is_TOKEN_TWO_OR_MORE() {
        /* given */

        String title = "완벽한 크리스마스를 보내는 방법 ";

        Map<Token, List<String>> tokenMap = getTokenMap(List.of("완벽", "크리스마스", "방법"),List.of("보내는"));

        when(filterStream.doFiltering(title, true)).thenReturn(title);
        when(tokenizer.tokenize(title)).thenReturn(tokenMap);

        /* when */

        TitleQuery sut = analyzer.analyze(title, true);

        /* then */
        assertEquals(sut.getTitleType(), TOKEN_TWO_OR_MORE);
    }

    @Test
    public void TitleQuery_is_ALL_ETC() {
        /* given */

        String title = "곰 세 마리";

        Map<Token, List<String>> tokenMap = getTokenMap(Collections.emptyList(),
            List.of("곰", "세", "마리"));

        when(filterStream.doFiltering(title, true)).thenReturn(title);
        when(tokenizer.tokenize(title)).thenReturn(tokenMap);

        /* when */
        TitleQuery sut = analyzer.analyze(title, true);

        /* then */
        assertEquals(sut.getTitleType(), TOKEN_ALL_ETC);
    }

    @Test
    public void TitleQuery_is_TOKEN_COMPLEX() {
        /* given */

        String title = "칠판 앞에 나가기 싫어!";

        Map<Token, List<String>> tokenMap = getTokenMap(List.of("칠판"),
            List.of("앞에", "나가기", "싫어"));

        when(filterStream.doFiltering(title, true)).thenReturn(title);
        when(tokenizer.tokenize(title)).thenReturn(tokenMap);

        /* when */
        TitleQuery sut = analyzer.analyze(title, true);

        /* then */

        assertEquals(sut.getTitleType(), TOKEN_COMPLEX);
    }

    private Map<Token, List<String>> getTokenMap(List<String> nnTokens, List<String> etcTokens) {
        Map<Token, List<String>> tokenMap = new HashMap<>();

        tokenMap.put(NN_TOKEN, nnTokens);
        tokenMap.put(ETC_TOKEN, etcTokens);

        return tokenMap;
    }

}