package com.scaling.libraryservice.search.util;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;
import org.springframework.stereotype.Component;

@Component
public class EunjeonTokenizer  {

    public Map<Language, List<String>> tokenize(String target) {

        List<String> tokenizedWords = new ArrayList<>();
        List<String> slWords = new ArrayList<>();

        Analyzer.parseJava(target).forEach(node ->
            {
                if (isNNP(node) || isNNG(node)) {
                    tokenizedWords.add(node.copy$default$1().surface());
                } else if (isSL(node)) {
                    slWords.add(node.copy$default$1().surface());
                }
            }
        );

        Map<Language,List<String>> map = new EnumMap<>(Language.class);
        map.put(Language.KOR,tokenizedWords);
        map.put(Language.ENG,slWords);

        return map;
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
