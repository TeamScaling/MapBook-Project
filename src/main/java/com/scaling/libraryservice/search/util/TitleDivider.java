package com.scaling.libraryservice.search.util;

import com.scaling.libraryservice.commons.timer.Timer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 제목을 영어와 한글로 나누어 분석하는 클래스입니다.
 */
public class TitleDivider {

    /**
     * 주어진 검색어를 영어와 한글로 분리하여 반환합니다.
     *
     * @param query 분리할 검색어
     * @return 영어와 한글로 분리된 검색어가 담긴 Map 객체 (키: "eng" - 영어, "kor" - 한글)
     */
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
                    if (c >= 46) {
                        engBuffer.append(c);
                        engFirst = false;
                    }

                }

            } else {

                if (!engBuffer.isEmpty()) {

                    eng.add(engBuffer.toString());
                    engBuffer.setLength(0);

                    korFirst = true;
                }

                if (c == ' ' & !korFirst) {
                    korBuffer.append(c);
                } else {
                    if (c > 47) {
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
