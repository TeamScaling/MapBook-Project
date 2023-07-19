package com.scaling.libraryservice.search.util;

import static com.scaling.libraryservice.search.util.Language.ENG;
import static com.scaling.libraryservice.search.util.Language.KOR;
import static com.scaling.libraryservice.search.util.TitleType.TOKEN_ONE;
import static com.scaling.libraryservice.search.util.TitleType.TOKEN_TWO_OR_MORE;

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

        Map<Language, List<String>> titleMap = tokenizer.tokenize(query);

        var titleQueryBuilder = TitleQuery.builder();

        AtomicInteger count = new AtomicInteger();

        titleMap.forEach((key, tokens) -> {

            if (key == KOR) {
                count.addAndGet(tokens.size());
                titleQueryBuilder.korToken(String.join(" ", tokens));

            } else if(key == ENG) {

                count.addAndGet(tokens.size());
                titleQueryBuilder.engToken(String.join(" ", tokens));
            }
        });

        return count.get() == TOKEN_SIZE_DEFAULT ?
            titleQueryBuilder.titleType(TOKEN_ONE).build()
            : titleQueryBuilder.titleType(TOKEN_TWO_OR_MORE).build();
    }
}
