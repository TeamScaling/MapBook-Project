package com.scaling.libraryservice.search.engine.filter;

import com.scaling.libraryservice.search.exception.NotQualifiedQueryException;

public abstract class AbstractTileFilter implements TitleFilter {

    public AbstractTileFilter() {
    }


    public abstract String filtering(String query);

    protected String progressFilter(String query, TitleFilter nextFilter)
        throws NotQualifiedQueryException {

        return nextFilter != null ? nextFilter.filtering(query) : query;
    }

}
