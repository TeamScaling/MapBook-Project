package com.scaling.libraryservice.search.util;

import static com.scaling.libraryservice.search.util.TitleDivider.Language.KOR;
import static com.scaling.libraryservice.search.util.TitleType.TOKEN_ONE;
import static com.scaling.libraryservice.search.util.TitleType.TOKEN_TWO_OR_MORE;

import com.scaling.libraryservice.search.util.TitleDivider.Language;
import java.util.Map;
import java.util.StringJoiner;
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

    private final TitleTokenizer tokenizer;

    private static final int TOKEN_SIZE_LIMIT = 2;

    public TitleQuery analyze(String query) {

        Map<Language, String> titleMap = TitleDivider.divideKorEng(query);

        StringJoiner joiner = new StringJoiner(" ");

        var titleQueryBuilder = TitleQuery.builder();

        titleMap.forEach((key, tokens) -> {

            if(key == KOR){
                titleQueryBuilder.korToken(getKorToken(tokens));
            }else{
                titleQueryBuilder.engToken(tokens);
            }

            joiner.add(tokens);
        });

        String result = joiner.toString().trim();

        if (result.split(" ").length >= TOKEN_SIZE_LIMIT) {
            return titleQueryBuilder.titleType(TOKEN_TWO_OR_MORE).build();
        } else {
            return titleQueryBuilder.titleType(TOKEN_ONE).build();
        }
    }

    private String getKorToken(String korToken) {

        if (!korToken.isEmpty()) {
            return String.join(" ",tokenizer.tokenize(korToken));
        } else {
            return korToken;
        }
    }
}
