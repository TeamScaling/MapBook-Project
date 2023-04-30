package com.scaling.libraryservice.search.cacheKey;

import com.scaling.libraryservice.caching.CacheKey;
import java.util.Objects;

public class BookCacheKey implements CacheKey {

    private final String query;

    private final int page;

    public String getQuery() {
        return query;
    }

    public int getPage() {
        return page;
    }

    public BookCacheKey(String query, int page) {
        this.query = query;
        this.page = page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookCacheKey that = (BookCacheKey) o;
        return page == that.page && Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, page);
    }
}
