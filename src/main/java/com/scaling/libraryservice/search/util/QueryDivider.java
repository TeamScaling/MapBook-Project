package com.scaling.libraryservice.search.util;

import com.scaling.libraryservice.aop.Timer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryDivider {

    @Timer
    public static Map<String, List<String>> divideTitle(String query) {

        char[] chars = query.toCharArray();

        StringBuilder engBuffer = new StringBuilder();
        StringBuilder korBuffer = new StringBuilder();

        List<String> eng = new ArrayList<>();
        List<String> kor = new ArrayList<>();

        boolean engFirst = true;
        boolean korFirst = true;

        for (char c : chars) {

            if (c <= 122 & c >= 46) {
                if (!korBuffer.isEmpty()) {
                    kor.add(korBuffer.toString());
                    korBuffer.setLength(0);

                    engFirst = true;
                }

                if (c == ' ' & engFirst) {

                } else {
                    if(c >=46){
                        engBuffer.append(c);
                        engFirst = false;
                    }

                }

            } else  {

                if (!engBuffer.isEmpty()) {

                    eng.add(engBuffer.toString());
                    engBuffer.setLength(0);

                    korFirst = true;
                }

                if (c == ' ' & !korFirst) {
                    korBuffer.append(c);
                } else {
                    if(c >47){
                        korBuffer.append(c);
                        korFirst = false;
                    }
                }



            }
        }

        if (!engBuffer.isEmpty()) {
            eng.add(engBuffer.toString());
        }

        if (!korBuffer.isEmpty()) {
            kor.add(korBuffer.toString());
        }

        Map<String, List<String>> result = new HashMap<>();

        result.put("eng", eng);
        result.put("kor", kor);


        return result;
    }

}
