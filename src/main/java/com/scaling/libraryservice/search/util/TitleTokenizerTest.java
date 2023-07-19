package com.scaling.libraryservice.search.util;

import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.Eojeol;
import org.bitbucket.eunjeon.seunjeon.LNode;

public class TitleTokenizerTest {

    public static void main(String[] args) {

        for (LNode node : Analyzer.parseJava("java 정석spring")) {
            System.out.println( node.copy$default$1().getFeatureHead());
            System.out.println(node);
        }

//        System.out.println("---------------------");
//        // 어절 분석
//        for (Eojeol eojeol: Analyzer.parseEojeolJava("무기팔지마세요")) {
//            System.out.println(eojeol);
//            for (LNode node: eojeol.nodesJava()) {
//                System.out.println(node);
//            }
//        }

//        EunjeonTokenizer tokenizer = new EunjeonTokenizer();
//
//        var result = tokenizer.tokenize("무기팔지마세요");
//
//        System.out.println(result);
    }

}
