package com.scaling.libraryservice.search.util.filter;

public abstract class AbstractTileFilter implements TitleFilter{

    public AbstractTileFilter() {
    }


    public abstract String filtering(String query);

    protected String progressFilter(String query, TitleFilter nextFilter){
       return nextFilter != null? nextFilter.filtering(query) : query;
    }
}
