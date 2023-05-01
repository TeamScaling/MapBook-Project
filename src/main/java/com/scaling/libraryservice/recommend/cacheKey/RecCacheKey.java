package com.scaling.libraryservice.recommend.cacheKey;

import com.scaling.libraryservice.commons.caching.CacheKey;
import java.util.Objects;

public class RecCacheKey implements CacheKey {

    private final String query;

    public RecCacheKey(String query) {
        this.query = query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecCacheKey that = (RecCacheKey) o;
        return Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query);
    }
}
