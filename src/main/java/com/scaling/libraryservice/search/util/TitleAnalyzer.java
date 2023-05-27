package com.scaling.libraryservice.search.util;

import static com.scaling.libraryservice.search.domain.TitleType.ENG_KOR_MT;
import static com.scaling.libraryservice.search.domain.TitleType.ENG_KOR_SG;
import static com.scaling.libraryservice.search.domain.TitleType.ENG_MT;
import static com.scaling.libraryservice.search.domain.TitleType.ENG_SG;
import static com.scaling.libraryservice.search.domain.TitleType.KOR_ENG;
import static com.scaling.libraryservice.search.domain.TitleType.KOR_MT_OVER_TWO;
import static com.scaling.libraryservice.search.domain.TitleType.KOR_MT_UNDER_TWO;
import static com.scaling.libraryservice.search.domain.TitleType.KOR_SG;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.search.domain.TitleQuery;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    private final TitleTokenizer titleTokenizer;

    /**
     * 주어진 검색어를 분석하여 TitleQuery 객체를 반환합니다.
     *
     * @param query 분석할 검색어
     * @return 분석된 검색어 정보를 담은 TitleQuery 객체
     */
    @Timer
    public TitleQuery analyze(String query) {
        query = removeKeyword(query);

        if (isEnglish(query)) {

            log.info("english title : [{}]", query);

            return queryResolve(query, false);

        } else if (isKorean(query)) {

            log.info("korean title : [{}]", query);

            return queryResolve(query, true);
        } else {
            log.info("korean & english title : [{}]", query);
            return engKorResolve(query);
        }
    }

    //consider : 쿼리를 변형 하는 작업이 과연 분석기의 역할에 합당 할까?
    String removeKeyword(String query) {

        String[] keyWord = {"이야기", "장편소설", "한국사"};

        if (query.split(" ").length > 1) {

            for (String key : keyWord) {
                log.info("delete keyword [{}] in [{}]", key, query);
                query = query.replaceAll(key, "");
            }
        }

        return query.trim();
    }

    private TitleQuery queryResolve(String query, boolean isKor) {
        int queryWordCount = query.split(" ").length;

        if (queryWordCount == 1) {
            return resolveSingleQuery(query, isKor);
        }

        if (isKor) {
            return resolveMultiKorQuery(query, queryWordCount);
        }

        return resolveMultiEngQuery(query);
    }

    private TitleQuery resolveSingleQuery(String query, boolean isKor) {
        return isKor
            ? TitleQuery.builder().titleType(KOR_SG).korToken(query).build()
            : TitleQuery.builder().titleType(ENG_SG).engToken(query).build();
    }

    private TitleQuery resolveMultiKorQuery(String query, int queryWordCount) {
        if (queryWordCount > 2) {

            return TitleQuery.builder().titleType(KOR_MT_OVER_TWO).korToken(query).build();
        }

        query = splitTarget(query);
        return TitleQuery.builder().titleType(KOR_MT_UNDER_TWO).korToken(query).build();

    }

    private TitleQuery resolveMultiEngQuery(String query) {
        query = splitTarget(query);

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

        if (korToken.length() >= engToken.length()) {
            return resolveKorLongerCase(korToken, engToken);
        } else {
            return resolveEngLongerCase(korToken, engToken);
        }
    }

    private String getKorToken(Map<String, List<String>> titleMap) {
        List<String> korTokens = titleMap.get("kor");
        String korToken = String.join(" ", korTokens);
        if (!korToken.isEmpty()) {
            List<String> nnKorTokens = titleTokenizer.tokenize(korToken);
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

    private TitleQuery resolveKorLongerCase(String korToken, String engToken) {
        korToken = splitTarget(korToken);

        if (engToken.isEmpty()) {

            return TitleQuery.builder().titleType(KOR_MT_UNDER_TWO).korToken(korToken).build();
        }

        return TitleQuery.builder().titleType(KOR_ENG).engToken(engToken).korToken(korToken).build();
    }

    private TitleQuery resolveEngLongerCase(String korToken, String engToken) {
        if (korToken.split(" ").length <= 1) {
            korToken = splitTarget(korToken);

            return TitleQuery.builder().titleType(ENG_KOR_SG).engToken(engToken).korToken(korToken).build();
        } else {

            return TitleQuery.builder().titleType(ENG_KOR_MT).engToken(engToken).korToken(korToken).build();
        }
    }


    /**
     * 정규식을 이용하여 영어 제목인지 판별 합니다.
     *
     * @param input 판별하고자 하는 제목
     * @return 영어 제목이면 true, 그 외에는 false;
     */
    public static boolean isEnglish(String input) {
        String pattern = "^[a-zA-Z0-9\\.\\s:,;?!\\-()\\[\\]{}<>]+$";
        return input.matches(pattern);
    }

    /**
     * 정규식을 이용하여 한글 제목인지 판별 합니다.
     *
     * @param input 판별하고자 하는 제목
     * @return 한글 제목이면 true, 그 외에는 false
     */
    public static boolean isKorean(String input) {
        String pattern = "^[가-힣0-9\\.\\s:,;?!\\-()\\[\\]{}<>]+$";
        return input.matches(pattern);
    }

    /**
     * 주어진 제목 문자열을 나눈 뒤 다른 문자를 더해 알맞게 변형 합니다.
     *
     * @param target 변형하고자 하는 제목 문자열
     * @return 변형된 제목 문자열
     */
    private String splitTarget(String target) {
        return Arrays.stream(target.split(" "))
            .map(name -> "+" + name)
            .collect(Collectors.joining(" "));
    }

}
