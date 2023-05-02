package com.scaling.libraryservice.search.util;

import static com.scaling.libraryservice.search.domain.TitleType.ENG_KOR_MT;
import static com.scaling.libraryservice.search.domain.TitleType.ENG_KOR_SG;
import static com.scaling.libraryservice.search.domain.TitleType.ENG_MT;
import static com.scaling.libraryservice.search.domain.TitleType.ENG_SG;
import static com.scaling.libraryservice.search.domain.TitleType.KOR_ENG;
import static com.scaling.libraryservice.search.domain.TitleType.KOR_MT;
import static com.scaling.libraryservice.search.domain.TitleType.KOR_SG;

import com.scaling.libraryservice.search.domain.TitleQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 검색어에 대한 분석을 수행하는 클래스입니다.
 */
@Slf4j @Component
@RequiredArgsConstructor
public class TitleAnalyzer {

    private final TitleTokenizer titleTokenizer;

    /**
     * 주어진 검색어를 분석하여 TitleQuery 객체를 반환합니다.
     *
     * @param query 분석할 검색어
     * @return 분석된 검색어 정보를 담은 TitleQuery 객체
     */
    public TitleQuery analyze(String query) {

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
    /**
     * 주어진 검색어를 분석하여 TitleQuery 객체를 반환합니다.
     *
     * @param query 분석할 검색어
     * @param isKor 단일 한글 제목 유무
     * @return 분석된 검색어 정보를 담은 TitleQuery 객체
     */
    private TitleQuery queryResolve(String query,boolean isKor) {

        if (query.split(" ").length == 1) {

            if (isKor) {
                log.info("Single Kor query : [{}]", query);
                return new TitleQuery(KOR_SG,"",query,"") ;
            } else {
                log.info("Single Eng query : [{}]", query);
                return new TitleQuery(ENG_SG,query,"","") ;
            }

        } else {

            if (isKor) {
                query = splitTarget(query);
                log.info("Multi Kor query : [{}]", query);

                return new TitleQuery(KOR_MT,"",query,"");
            } else {
                query = splitTarget(query);
                log.info("Multi Eng query : [{}]", query);
                return new TitleQuery(ENG_MT,query,"","");
            }
        }
    }

    /**
     * 영어와 한글 혼합 제목인 경우 주어진 검색어를 분석하여 TitleQuery 객체를 반환합니다.
     *
     * @param query 분석할 검색어
     * @return 분석된 검색어 정보를 담은 TitleQuery 객체
     */
    private TitleQuery engKorResolve(String query) {

        Map<String, List<String>> titleMap = TitleDivider.divideTitle(query);

        List<String> engTokens = titleMap.get("eng");
        List<String> korTokens = titleMap.get("kor");

        String korToken = String.join(" ", korTokens);
        String engToken = String.join(" ", engTokens);

        StringBuilder engQueryBuilder = new StringBuilder();

        engTokens.forEach(t -> engQueryBuilder.append("%").append(t).append("% "));

        log.info("korToken : {}, length : {}", korToken, korToken.length());
        log.info("engToken : {}, length : {}", engToken, engToken.length());

        if (korToken.length() >= engToken.length()) {

            log.info("korToken >= engToken");

            return new TitleQuery(KOR_ENG,"","",query);

        } else {

            List<String> nnKorTokens = new ArrayList<>();

            // 한글 제목 내용을 명사 단위로만 검색 한다.
            if (!korToken.isEmpty()) {
                nnKorTokens = titleTokenizer.tokenize(korToken);
                korToken = String.join(" ", nnKorTokens);
            }

            if(nnKorTokens.size() <=1){
                korToken = splitTarget(korToken);

                log.info("[korToken < engToken] korToken : {} // engToken : {}", korToken,
                    engToken);


                return new TitleQuery(ENG_KOR_SG,engQueryBuilder.toString().trim(),korToken,"");
            }else{

                return new TitleQuery(ENG_KOR_MT,engQueryBuilder.toString().trim(),korToken,"");
            }
        }

    }

    /** 정규식을 이용하여 영어 제목인지 판별 합니다.
     *
     * @param input 판별하고자 하는 제목
     * @return 영어 제목이면 true, 그 외에는 false;
     */
    public static boolean isEnglish(String input) {
        String pattern = "^[a-zA-Z0-9\\.\\s]+$";
        return input.matches(pattern);
    }

    /** 정규식을 이용하여 한글 제목인지 판별 합니다.
     * 
     * @param input 판별하고자 하는 제목
     * @return 한글 제목이면 true, 그 외에는 false
     */
    public static boolean isKorean(String input) {
        String pattern = "^[가-힣0-9\\.\\s]+$";
        return input.matches(pattern);
    }

    /** 주어진 제목 문자열을 나눈 뒤 다른 문자를 더해 알맞게 변형 합니다.
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
