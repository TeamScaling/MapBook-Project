package com.scaling.libraryservice.search.util;

public class TitleFilter {
    public static String filtering(String query){
        query = query.toLowerCase();
        query = removeSpecialChar(query);
        return query;
    }

    // 특수문자를 제거 한다.
    private static String removeSpecialChar(String query) {
        return query.replaceAll("[^a-zA-Z0-9가-힣\\s]", "");
    }

}
