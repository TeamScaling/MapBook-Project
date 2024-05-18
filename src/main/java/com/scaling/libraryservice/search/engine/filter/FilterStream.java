package com.scaling.libraryservice.search.engine.filter;

public class FilterStream {

    private final TitleFilter titleFilter;

    public FilterStream(TitleFilter titleFilter) {
        this.titleFilter = titleFilter;
    }

    public String doFiltering(String query,boolean filterOn) {
        return  filterOn? titleFilter.filtering(query) : query;
    }
}
