package com.scaling.libraryservice.search.engine.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;


public class TitleTrimmer {


    /**
     * 주어진 제목 문자열을 나눈 뒤 다른 문자를 더해 알맞게 변형 합니다.
     *
     * @param target 변형하고자 하는 제목 문자열
     * @return 변형된 제목 문자열
     */
    public static String splitAddPlus(@NonNull String target) {
        target = target.trim();
        return Arrays.stream(target.split(" "))
            .map(name -> "+" + name)
            .collect(Collectors.joining(" "));
    }

    public static List<String> splitAddPlus(@NonNull List<String> target) {

        return target.stream().map(str -> "+"+str).toList();
    }

    /**
     * 도서 제목에서 불필요한 불용어를 제거하여 일정한 형태로 반환합니다.
     *
     * @param title 제거하고자 하는 도서 제목 문자열
     * @return 불용어가 제거된 도서 제목 문자열
     */
    public static String TrimTitleResult(@NonNull String title) {
        String[] titleParts = title.split(":");
        if (titleParts.length > 1) {
            String titlePrefix = titleParts[0];
            String[] titlePrefixParts = titlePrefix.trim().split("=");
            if (titlePrefixParts.length > 0) {
                titlePrefix = titlePrefixParts[0].trim();
            }
            return removeParentheses(removeDash(titlePrefix)).trim();
        }
        return removeParentheses(removeDash(title)).trim();
    }

    private static String removeParentheses(@NonNull String text) {
        return text.replaceAll("\\(.*?\\)|=.*$", "").trim();
    }

    private static String removeDash(@NonNull String text) {
        return text.replaceAll("-.*$", "").trim();
    }

}
