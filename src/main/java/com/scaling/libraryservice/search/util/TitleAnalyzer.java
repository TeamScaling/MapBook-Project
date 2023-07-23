package com.scaling.libraryservice.search.util;

import static com.scaling.libraryservice.search.util.Token.ETC_TOKEN;
import static com.scaling.libraryservice.search.util.Token.NN_TOKEN;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.search.util.TitleQuery.TitleQueryBuilder;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 검색어에 대한 분석을 수행하는 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TitleAnalyzer {

    private final EunjeonTokenizer tokenizer;

    private static final int TOKEN_MIN_SIZE = 1;

    @Timer
    public TitleQuery analyze(String query) {

        String originalQuery = query;

        Map<Token, List<String>> titleMap = tokenizer.tokenize(query);

        TitleQueryBuilder titleQueryBuilder = new TitleQueryBuilder();
        
        // 형태소 분석을 마친 결과를 TitleQuery의 멤버 변수에 맞게 각각 담는다.
        titleMap.forEach((key, tokens) -> {
            if (key == NN_TOKEN) {
                titleQueryBuilder.nnToken(String.join(" ", tokens));
            } else {
                titleQueryBuilder.etcToken(String.join(" ", tokens));
            }
        });

        // 어떤 검색 모드를 실시 할지 결정 한다.
        return resolveTitleQuery(
            titleMap.get(NN_TOKEN).size(),
            titleMap.get(ETC_TOKEN).size(),
            titleQueryBuilder,
            originalQuery);
    }

    private TitleQuery resolveTitleQuery(int nnCnt, int etcCnt,
        TitleQueryBuilder titleQueryBuilder, String query) {

        // 명사를 제외한 토큰들의 갯수가 최소를 넘으면
        return etcCnt >= TOKEN_MIN_SIZE ?
            analyzeForEtcTokens(nnCnt, query, titleQueryBuilder) :
            analyzeForNnTokens(nnCnt, titleQueryBuilder);
    }


    private TitleQuery analyzeForEtcTokens(int nnCnt, String query,
        TitleQueryBuilder titleQueryBuilder) {

        return nnCnt < TOKEN_MIN_SIZE ?
            // 명사 토큰의 갯수가 최소값을 넘지 못하면
            titleQueryBuilder.titleType(TitleType.TOKEN_ALL_ETC).etcToken(query).build()
            : titleQueryBuilder.titleType(TitleType.TOKEN_COMPLEX).build();
    }

    // 명사 토큰을 검색할 모드를 결정 한다( boolean or natural)
    private TitleQuery analyzeForNnTokens(int nnCnt,
        TitleQueryBuilder titleQueryBuilder) {
        
        return nnCnt == TOKEN_MIN_SIZE ?
            titleQueryBuilder.titleType(TitleType.TOKEN_ONE).build()
            : titleQueryBuilder.titleType(TitleType.TOKEN_TWO_OR_MORE).build();
    }

}
