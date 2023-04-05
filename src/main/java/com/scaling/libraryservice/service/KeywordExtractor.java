package com.scaling.libraryservice.service;

import java.util.List;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;


public class KeywordExtractor {

    public static void main(String[] args) {
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        String strToAnalyze = "토비스프링자바";

        KomoranResult analyzeResultList = komoran.analyze(strToAnalyze);

        List<Token> tokenList = analyzeResultList.getTokenList();

        List<String> nounList =
            tokenList.stream().filter(i -> i.getPos().equals("NNP") || i.getPos().equals("NNG"))
                .map(Token::getMorph).toList();

        System.out.println(nounList);

        /*Map<String, Integer> map = new HashMap<>();

        for (String s : nounList) {

            if (map.get(s) != null) {

                map.put(s, map.get(s) + 1);
            } else {

                map.put(s, 1);
            }
        }

        List<String> result
            = map.entrySet().stream()
                .filter(i -> i.getValue() > 1).map(e -> e.getKey()).toList();

        System.out.println(result);*/
    }

}
