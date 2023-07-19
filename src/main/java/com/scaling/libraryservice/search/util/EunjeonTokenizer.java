package com.scaling.libraryservice.search.util;

import com.scaling.libraryservice.search.util.TitleDivider.Language;
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
        List<String> remainWords = new ArrayList<>();
        List<String> slWords = new ArrayList<>();

        Analyzer.parseJava(target).forEach(node ->
            {
                if (isNNP(node) || isNNG(node)) {
                    tokenizedWords.add(node.copy$default$1().getSurface());
                } else if (isSL(node)) {
                    System.out.println(node);
                    slWords.add(node.copy$default$1().getSurface());
                } else {
                    remainWords.add(node.copy$default$1().getSurface());
                }
            }
        );

        Map<Language,List<String>> map = new EnumMap<>(Language.class);
        map.put(Language.KOR_N,tokenizedWords);
        map.put(Language.KOR_OTHER,remainWords);
        map.put(Language.ENG,slWords);

        return map;
    }

    public List<String> getEojeol(String target) {

        List<String> result = new ArrayList<>();

        Analyzer.parseEojeolJava(target).forEach(
            eojeol -> {
                result.add(eojeol.surface());
            }
        );

        return result;
    }

    private boolean isSL(LNode node) {
        return node.copy$default$1().getFeatureHead().equals("SL");
    }

    private boolean isNNP(LNode node) {
        return node.copy$default$1().getFeatureHead().equals("NNP");
    }

    private boolean isNNG(LNode node) {
        return node.copy$default$1().getFeatureHead().equals("NNG");
    }


}
