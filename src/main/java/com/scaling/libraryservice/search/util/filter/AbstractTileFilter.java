package com.scaling.libraryservice.search.util.filter;

import com.scaling.libraryservice.search.exception.NotQualifiedQueryException;

public abstract class AbstractTileFilter implements TitleFilter {

    private static final int QUERY_MIN_SIZE = 2;

    public AbstractTileFilter() {
    }


    public abstract String filtering(String query);

    protected String progressFilter(String query, TitleFilter nextFilter)
        throws NotQualifiedQueryException {

        checkValidation(query);
        return nextFilter != null ? nextFilter.filtering(query) : query;
    }

    private void checkValidation(String query) throws NotQualifiedQueryException{
        if (query.length() < QUERY_MIN_SIZE) {
            throw new NotQualifiedQueryException("공백이나 1글자는 못 찾아요"+"😅😅");
        }
    }
}
