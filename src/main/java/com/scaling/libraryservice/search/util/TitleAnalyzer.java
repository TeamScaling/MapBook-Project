package com.scaling.libraryservice.search.util;

import static com.scaling.libraryservice.search.util.TitleDivider.Language.ENG;
import static com.scaling.libraryservice.search.util.TitleDivider.Language.KOR_N;
import static com.scaling.libraryservice.search.util.TitleDivider.Language.KOR_OTHER;
import static com.scaling.libraryservice.search.util.TitleType.NGRAM;
import static com.scaling.libraryservice.search.util.TitleType.TOKEN_ONE;
import static com.scaling.libraryservice.search.util.TitleType.TOKEN_TWO_OR_MORE;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.search.util.TitleDivider.Language;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
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

//    @Timer
//    public TitleQuery analyze(String query) {
//
//        Map<Language, String> titleMap = TitleDivider.divideKorEng(query);
//
//        StringJoiner joiner = new StringJoiner(" ");
//
//        var titleQueryBuilder = TitleQuery.builder();
//
//        titleMap.forEach((key, tokens) -> {
//
//            if (key == KOR_N) {
//                titleQueryBuilder.korToken(getKorToken(tokens));
//            } else {
//                titleQueryBuilder.engToken(tokens);
//            }
//            joiner.add(tokens);
//        });
//
//        String result = joiner.toString().trim();
//
//        if (result.split(" ").length >= TOKEN_SIZE_DEFAULT) {
//            return titleQueryBuilder.titleType(TOKEN_TWO_OR_MORE).build();
//        } else {
//            return titleQueryBuilder.titleType(TOKEN_ONE).build();
//        }
//    }

    public TitleQuery analyze(String query) {

        Map<Language, List<String>> titleMap = tokenizer.tokenize(query);

        System.out.println(titleMap);

        if (isNGramMode(titleMap)) {
            return ngramSearchMode(query);
        } else {
            return spacedSearchMode(titleMap);
        }
    }

    private TitleQuery spacedSearchMode(Map<Language, List<String>> titleMap) {

        var titleQueryBuilder = TitleQuery.builder();

        AtomicInteger cnt = new AtomicInteger();

        titleMap.forEach((key, tokens) -> {

            if (key == KOR_N) {
                titleQueryBuilder.korToken(String.join(" ", tokens));
            } else if(key == ENG) {
                titleQueryBuilder.engToken(String.join(" ", tokens));
            }
            cnt.incrementAndGet();
        });

        return cnt.get() == TOKEN_SIZE_DEFAULT ?
            titleQueryBuilder.titleType(TOKEN_ONE).build()
            : titleQueryBuilder.titleType(TOKEN_TWO_OR_MORE).build();
    }

    private TitleQuery ngramSearchMode(String query) {

        return TitleQuery.builder()
            .titleType(NGRAM)
            .korToken(
                String.join(" ", tokenizer.getEojeol(query))
            ).build();
    }

    private boolean isNGramMode(Map<Language, List<String>> titleMap) {

        if (!titleMap.get(ENG).isEmpty()) {
            System.out.println("hello");
            return false;
        }

        List<String> nnkorWords = titleMap.get(KOR_N);
        List<String> remainWords = titleMap.get(KOR_OTHER);

        if(nnkorWords.size() >= 2){
            return false;
        }

        return nnkorWords.size() < remainWords.size();
    }
}
