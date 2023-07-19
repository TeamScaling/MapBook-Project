package com.scaling.libraryservice.search.util;

import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;

public class TokenizerTest2 {

    public static void main(String[] args) {

        String target = "살인자의 기억법 :김영하 장편소설";

        for (LNode node : Analyzer.parseJava(target)) {

            System.out.println(node);
        }


    }

}
