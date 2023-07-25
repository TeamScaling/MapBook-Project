package com.scaling.libraryservice.search.util.filter;

import com.scaling.libraryservice.search.exception.NotQualifiedQueryException;

public abstract class AbstractTileFilter implements TitleFilter {

    public AbstractTileFilter() {
    }


    public abstract String filtering(String query);

    protected String progressFilter(String query, TitleFilter nextFilter)
        throws NotQualifiedQueryException {

//        if (query.isEmpty()) {
//            throw new NotQualifiedQueryException("잘못된 검색어입니다");
//        }

        return nextFilter != null ? nextFilter.filtering(query) : query;
    }
}
