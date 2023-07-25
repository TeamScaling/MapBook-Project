package com.scaling.libraryservice.search.util.filter;

public class FilterStream {

    TitleFilter titleFilter;

    public FilterStream(TitleFilter titleFilter) {
        this.titleFilter = titleFilter;
    }

    public String doFiltering(String query) {
        return titleFilter.filtering(query);
    }
}
