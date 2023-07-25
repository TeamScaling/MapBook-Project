package com.scaling.libraryservice.search.util.filter;

import org.springframework.stereotype.Component;


public class SimpleFilter extends AbstractTileFilter implements TitleFilter {

    private final TitleFilter nextFilter;

    public SimpleFilter(TitleFilter nextFilter) {
        this.nextFilter = nextFilter;
    }



    @Override
    public String filtering(String query) {

        return progressFilter(
            removeSpecialChar(
                query.trim().toLowerCase()
            ),
            this.nextFilter
        );
    }

    // 특수문자를 제거 한다.
    private String removeSpecialChar(String query) {
        return query.replaceAll("[^a-zA-Z0-9가-힣\\s]", "");
    }
}
