package com.scaling.libraryservice.commons.caching.mock;

import com.github.benmanes.caffeine.cache.Cache;
import com.scaling.libraryservice.commons.caching.CacheKey;
import java.util.Objects;

public class NameCacheKey implements CacheKey<NameCacheKey,UserInfo> {

    private final String name;

    private final int age;

    public NameCacheKey(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public Cache<? extends CacheKey<NameCacheKey, UserInfo>, UserInfo> configureCache() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NameCacheKey that = (NameCacheKey) o;
        return age == that.age && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
