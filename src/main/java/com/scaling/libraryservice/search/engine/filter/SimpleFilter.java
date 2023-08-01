package com.scaling.libraryservice.search.engine.filter;

import com.scaling.libraryservice.search.exception.NotQualifiedQueryException;

public class SimpleFilter extends AbstractTileFilter implements TitleFilter {

    private final TitleFilter nextFilter;
    private static final String ALLOWED_CHARS_REGEX = "[^a-zA-Z0-9가-힣\\s]";

    private static final int QUERY_MIN_SIZE = 2;


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
    String removeSpecialChar(String query) {
        query = query.replaceAll(ALLOWED_CHARS_REGEX + "|\\s+", " ");
        checkValidation(query);
        return query;
    }

    private void checkValidation(String query) throws NotQualifiedQueryException {
        if (query.length() < QUERY_MIN_SIZE) {
            throw new NotQualifiedQueryException("공백이나 1글자는 못 찾아요" + "😅😅");
        }
    }
}
