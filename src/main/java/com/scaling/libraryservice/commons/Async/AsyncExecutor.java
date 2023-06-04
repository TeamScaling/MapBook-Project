package com.scaling.libraryservice.commons.Async;

import java.util.function.Supplier;

public interface AsyncExecutor<T,V> {

    T execute(Supplier<T> supplier,V value,int timeout);
}
