package com.scaling.libraryservice.search.util;

import static com.scaling.libraryservice.search.util.TitleType.ENG_KOR_MT;
import static com.scaling.libraryservice.search.util.TitleType.ENG_KOR_SG;
import static com.scaling.libraryservice.search.util.TitleType.ENG_MT;
import static com.scaling.libraryservice.search.util.TitleType.ENG_SG;
import static com.scaling.libraryservice.search.util.TitleType.KOR_MT_OVER_TWO;
import static com.scaling.libraryservice.search.util.TitleType.KOR_MT_UNDER_TWO;
import static com.scaling.libraryservice.search.util.TitleType.KOR_SG;

import com.scaling.libraryservice.commons.timer.Timer;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 검색어에 대한 분석을 수행하는 클래스입니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TitleAnalyzer {

    private final TitleTokenizer tokenizer;


    /**
     * 주어진 검색어를 분석하여 TitleQuery 객체를 반환합니다.
     *
     * @param query 분석할 검색어
     * @return 분석된 검색어 정보를 담은 TitleQuery 객체
     */
    @Timer
    public TitleQuery analyze(String query) {
        query = TitleTrimmer.removeKeyword(query);

        TitleQuery titleQuery;

        if (isEnglish(query)) {

            titleQuery = queryResolve(query, false);

        } else if (isKorean(query)) {

            titleQuery = queryResolve(query, true);
        } else {
            titleQuery = engKorResolve(query);
        }

        log.info("Query is [{}] and tokens : [{}]", titleQuery.getTitleType().name(), titleQuery);

        return titleQuery;
    }


    private TitleQuery queryResolve(String query, boolean isKor) {
        int queryWordCount = query.split(" ").length;

        if (queryWordCount == 1) {
            return resolveSingleQuery(query, isKor);
        } else if (queryWordCount > 1 & isKor) {
            return resolveMultiKorQuery(query, queryWordCount);
        } else {
            return resolveMultiEngQuery(query);
        }
    }

    private TitleQuery resolveSingleQuery(String query, boolean isKor) {
        return isKor
            ? TitleQuery.builder().titleType(KOR_SG).korToken(query).build()
            : TitleQuery.builder().titleType(ENG_SG).engToken(query).build();
    }

    private TitleQuery resolveMultiKorQuery(String query, int queryWordCount) {

        if (queryWordCount > 2) {
            return TitleQuery.builder().titleType(KOR_MT_OVER_TWO).korToken(query).build();
        }else{
            query = TitleTrimmer.splitAddPlus(query);
            return TitleQuery.builder().titleType(KOR_MT_UNDER_TWO).korToken(query).build();
        }
    }

    private TitleQuery resolveMultiEngQuery(String query) {
        query = TitleTrimmer.splitAddPlus(query);

        return TitleQuery.builder().titleType(ENG_MT).engToken(query).build();
    }


    /**
     * 영어와 한글 혼합 제목인 경우 주어진 검색어를 분석하여 TitleQuery 객체를 반환합니다.
     *
     * @param query 분석할 검색어
     * @return 분석된 검색어 정보를 담은 TitleQuery 객체
     */
    private TitleQuery engKorResolve(String query) {
        Map<String, List<String>> titleMap = TitleDivider.divideKorEng(query);

        String korToken = getKorToken(titleMap);
        String engToken = getEngToken(titleMap);

        if(titleMap.get("kor").size()>1){
            return TitleQuery.builder().titleType(ENG_KOR_MT).engToken(engToken).korToken(korToken)
                .build();
        }else{
            korToken = TitleTrimmer.splitAddPlus(korToken);

            return TitleQuery.builder().titleType(ENG_KOR_SG).engToken(engToken).korToken(korToken)
                .build();
        }
    }

    private String getKorToken(Map<String, List<String>> titleMap) {
        List<String> korTokens = titleMap.get("kor");
        String korToken = String.join(" ", korTokens);

        if (!korToken.isEmpty()) {
            List<String> nnKorTokens = tokenizer.tokenize(korToken);
            if (!nnKorTokens.isEmpty()) {
                korToken = String.join(" ", nnKorTokens);
            }
        }

        return korToken;
    }

    private String getEngToken(Map<String, List<String>> titleMap) {
        return titleMap.get("eng").stream()
            .max(Comparator.comparing(String::length))
            .map(t -> "%" + t + "% ")
            .orElse("");
    }



    /**
     * 정규식을 이용하여 영어 제목인지 판별 합니다.
     *
     * @param input 판별하고자 하는 제목
     * @return 영어 제목이면 true, 그 외에는 false;
     */
    private boolean isEnglish(String input) {
        String pattern = "^[a-zA-Z0-9\\.\\s:,;?!\\-()\\[\\]{}<>=]+$";
        return input.matches(pattern);
    }

    /**
     * 정규식을 이용하여 한글 제목인지 판별 합니다.
     *
     * @param input 판별하고자 하는 제목
     * @return 한글 제목이면 true, 그 외에는 false
     */
    private boolean isKorean(String input) {
        String pattern = "^[가-힣0-9\\.\\s:,;?!\\-()\\[\\]{}<>=]+$";
        return input.matches(pattern);
    }



}
