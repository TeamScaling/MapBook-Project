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

@Slf4j @Component
@RequiredArgsConstructor
public class TitleAnalyzer {

    private final TitleTokenizer titleTokenizer;

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

    public static boolean isEnglish(String input) {
        String pattern = "^[a-zA-Z0-9\\.\\s]+$";
        return input.matches(pattern);
    }

    public static boolean isKorean(String input) {
        String pattern = "^[가-힣0-9\\.\\s]+$";
        return input.matches(pattern);
    }

    private String splitTarget(String target) {
        return Arrays.stream(target.split(" "))
            .map(name -> "+" + name)
            .collect(Collectors.joining(" "));
    }

}
