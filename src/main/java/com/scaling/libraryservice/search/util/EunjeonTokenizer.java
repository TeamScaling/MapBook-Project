package com.scaling.libraryservice.search.util;

import static com.scaling.libraryservice.search.util.Token.ETC_TOKEN;
import static com.scaling.libraryservice.search.util.Token.NN_TOKEN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;
import org.springframework.stereotype.Component;

@Component
public class EunjeonTokenizer {

    public Map<Token, List<String>> tokenize(String target) {

        List<String> tokenizedWords = getNnTokens(target);

        // 사용자 검색 쿼리에서 명사 부분을 제외한 나머지를 추출 한다.
        target = getEtcTokens(tokenizedWords, target);

        Map<Token, List<String>> map = new EnumMap<>(Token.class);
        map.put(NN_TOKEN, tokenizedWords);

        // 문장을 제외한 나머지 부분이 길이가 2 이상인 경우에만 담는다.
        if (!target.isBlank() && target.length() > 1) {
            map.put(ETC_TOKEN,
                Arrays.stream(target.split(" ")).filter(s -> s.length() > 1).toList());
        } else {
            map.put(ETC_TOKEN, Collections.emptyList());
        }

        return map;
    }

    private List<String> getNnTokens(String target) {

        List<String> tokenizedWords = new ArrayList<>();

        // 자연어 분석기를 사용하여 명사,외국어를 추출한다.
        Analyzer.parseJava(target).forEach(node ->
            {
                if (isNNP(node) || isNNG(node) || isSL(node)) {

                    String surface = node.copy$default$1().surface();

                    if (surface.length() > 1) {
                        tokenizedWords.add(surface);
                    }
                }
            }
        );

        return tokenizedWords;
    }

    private String getEtcTokens(List<String> nnWords, String target) {
        for (String str : nnWords) {
            target = target.replaceAll(str, "").trim();
        }

        return target;
    }

    private boolean hasFeatureHead(LNode node, String feature) {

        return node.copy$default$1().feature().head().equals(feature);
    }

    private boolean isSL(LNode node) {
        return hasFeatureHead(node, "SL");
    }

    private boolean isNNP(LNode node) {
        return hasFeatureHead(node, "NNP");
    }

    private boolean isNNG(LNode node) {
        return hasFeatureHead(node, "NNG");
    }

}
