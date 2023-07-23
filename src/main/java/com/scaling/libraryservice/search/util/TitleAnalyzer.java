package com.scaling.libraryservice.search.util;

import static com.scaling.libraryservice.search.util.Token.NN_TOKEN;

import com.scaling.libraryservice.commons.timer.Timer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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

    private static final int TOKEN_SIZE_DEFAULT = 1;

    @Timer
    public TitleQuery analyze(String query) {

        Map<Token, List<String>> titleMap = tokenizer.tokenize(query);

        TitleQuery.TitleQueryBuilder titleQueryBuilder = TitleQuery.builder();

        AtomicInteger nTokens = new AtomicInteger();
        AtomicInteger etcTokens = new AtomicInteger();

        titleMap.forEach((key, tokens) -> {

            if (key == NN_TOKEN) {
                nTokens.addAndGet(tokens.size());
                titleQueryBuilder.nnToken(String.join(" ", tokens));

            } else {

                etcTokens.addAndGet(tokens.size());
                if (!tokens.isEmpty()) {
                    titleQueryBuilder.etcToken(String.join(" ", tokens));
                }
            }
        });

        int nnCnt = nTokens.get();
        int etcCnt = etcTokens.get();

        if (etcCnt > 0) {
            return titleQueryBuilder.titleType(TitleType.TOKEN_COMPLEX).build();
        } else {

            if (nnCnt == TOKEN_SIZE_DEFAULT) {
                return titleQueryBuilder.titleType(TitleType.TOKEN_ONE).build();
            } else {
                return titleQueryBuilder.titleType(TitleType.TOKEN_TWO_OR_MORE).build();
            }
        }
    }
}
