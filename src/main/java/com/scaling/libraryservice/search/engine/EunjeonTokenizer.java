package com.scaling.libraryservice.search.engine;

import static com.scaling.libraryservice.search.engine.Token.ETC_TOKEN;
import static com.scaling.libraryservice.search.engine.Token.NN_TOKEN;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class EunjeonTokenizer {

    private final static int TOKEN_MIN_SIZE = 2;

    private final static String ENG_FEATURE = "SL";

    private final static String KOR_NNP_FEATURE = "NNP";

    private final static String KOR_NNG_FEATURE = "NNG";

    public Map<Token, List<String>> tokenize(String target) {

        // 검색어에서 명사만 추출 한다.
        List<String> tokenizedWords = getQualifiedNnTokens(target);

        // 사용자 검색어에서 명사 부분을 제외한 나머지를 추출 한다.
        target = getEtcTokens(tokenizedWords, target);

        // 형태소 분석을 마친 결과값을 map에 담는다.
        Map<Token, List<String>> resultMap = new EnumMap<>(Token.class);
        resultMap.put(NN_TOKEN, tokenizedWords);

        // 명사를 제외한 나머지 어절을 조건에 맞게 담는다.
        filterAndStoreEtcTokens(target, resultMap);

        return resultMap;
    }

    // 명사를 제외한 나머지 어절을 조건에 맞게 담는다.
    private void filterAndStoreEtcTokens(String target, Map<Token, List<String>> resultMap) {
        resultMap.put(ETC_TOKEN,
            Arrays.stream(
                    target.split(" "))
                .filter(EunjeonTokenizer::isQualifiedToken)
                .toList());
    }

    // 형태소 분석기로 분석 한 뒤 적합한 어절을 최소 사이즈 이상만 List에 담아 반환
    public static List<String> getQualifiedNnTokens(String target) {

        return Analyzer.parseJava(target)
            .stream()
            .filter(EunjeonTokenizer::isQualifiedNode)
            .map(node -> node.copy$default$1().surface())
            .filter(EunjeonTokenizer::isQualifiedToken)
            .toList();
    }


    // 최소 사이즈를 넘는 토큰과 영어,한글,숫자만 유효한 토큰으로 결정
    static boolean isQualifiedToken(String token) {
        return token.length() >= TOKEN_MIN_SIZE;
    }

    static boolean isQualifiedNode(LNode node) {
        return isNNP(node) || isNNG(node) || isSL(node);
    }

    String getEtcTokens(List<String> nnWords, String target) {

        Set<String> uniqueWords
            = new LinkedHashSet<>(Arrays.asList(target.split(" ")));

        nnWords.forEach(
            nnWord -> uniqueWords.removeIf(splitWord -> splitWord.contains(nnWord)));

        return String.join(" ", uniqueWords).trim();
    }

    private static boolean hasFeatureHead(LNode node, String feature) {

        return node.copy$default$1().feature().head().equals(feature);
    }

    private static boolean isSL(LNode node) {
        return hasFeatureHead(node, ENG_FEATURE);
    }

    private static boolean isNNP(LNode node) {
        return hasFeatureHead(node, KOR_NNP_FEATURE);
    }

    private static boolean isNNG(LNode node) {
        return hasFeatureHead(node, KOR_NNG_FEATURE);
    }

}
