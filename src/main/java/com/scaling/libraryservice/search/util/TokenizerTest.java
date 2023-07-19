package com.scaling.libraryservice.search.util;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;
import org.springframework.util.StopWatch;

public class TokenizerTest {

    public static void main(String[] args) {

        String target = "살인자의 기억법 :김영하 장편소설";

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        boolean first = true;
        for (LNode node : Analyzer.parseJava(target)) {
            if(first){
                System.out.println("은전한닢 분석 : "+target);
                first = false;
            }

            System.out.println(node);
        }

        stopWatch.stop();
        String totalTime = String.format("%.6f",stopWatch.getTotalTimeSeconds());
        System.out.println("은전한닢 분석 시간 "+totalTime);

        System.out.println("--------------------------------------");

        KomoranTokenizer tokenizer = new KomoranTokenizer(new Komoran(DEFAULT_MODEL.FULL));

        stopWatch.start();
        System.out.println("Komoran 분석 : "+target);
        tokenizer.tokenize(target);

        stopWatch.stop();

        totalTime = String.format("%.6f",stopWatch.getTotalTimeSeconds());
        System.out.println("Komoran 분석 시간 "+totalTime);

    }

}
