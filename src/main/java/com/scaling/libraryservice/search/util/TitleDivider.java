package com.scaling.libraryservice.search.util;

import com.scaling.libraryservice.commons.timer.Timer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.NonNull;

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
    public static Map<String, List<String>> divideKorEng(@NonNull String query) {
        char[] chars = query.toCharArray();

        StringBuilder engBuilder = new StringBuilder();
        StringBuilder korBuilder = new StringBuilder();

        List<String> eng = new ArrayList<>();
        List<String> kor = new ArrayList<>();

        for (char c : chars) {
            if (c >= 'A' && c <= 'z') {
                if (korBuilder.length() > 0) {
                    kor.add(korBuilder.toString());
                    korBuilder.setLength(0);
                }
                engBuilder.append(c);
            } else if (c >= '가' && c <= '힣') {
                if (engBuilder.length() > 0) {
                    eng.add(engBuilder.toString());
                    engBuilder.setLength(0);
                }
                korBuilder.append(c);
            } else if (c == ' ') {
                if (engBuilder.length() > 0) {
                    eng.add(engBuilder.toString());
                    engBuilder.setLength(0);
                }
                if (korBuilder.length() > 0) {
                    kor.add(korBuilder.toString());
                    korBuilder.setLength(0);
                }
            }
        }

        if (engBuilder.length() > 0) {
            eng.add(engBuilder.toString());
        }
        if (korBuilder.length() > 0) {
            kor.add(korBuilder.toString());
        }

        Map<String, List<String>> result = new HashMap<>();
        result.put("eng", eng);
        result.put("kor", kor);

        return result;
    }


}
