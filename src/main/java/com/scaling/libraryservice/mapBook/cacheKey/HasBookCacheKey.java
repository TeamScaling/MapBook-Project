package com.scaling.libraryservice.mapBook.cacheKey;

import com.scaling.libraryservice.caching.CacheKey;
import java.util.Objects;

public class HasBookCacheKey implements CacheKey {

    private String isbn13;

    private Integer areaCd;

    public HasBookCacheKey(String isbn13, Integer areaCd) {
        this.isbn13 = isbn13;
        this.areaCd = areaCd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HasBookCacheKey that = (HasBookCacheKey) o;
        return Objects.equals(isbn13, that.isbn13) && Objects.equals(areaCd,
            that.areaCd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn13, areaCd);
    }
}
