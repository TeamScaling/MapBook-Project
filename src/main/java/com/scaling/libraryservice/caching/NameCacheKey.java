package com.scaling.libraryservice.caching;

import java.util.Objects;

public class NameCacheKey implements CacheKey{

    private String name;

    private int age;

    public NameCacheKey(String name, int age) {
        this.name = name;
        this.age = age;
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
