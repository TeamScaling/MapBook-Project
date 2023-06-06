package com.scaling.libraryservice.mapBook.service.location;

public interface LocationResolver<T,V> {

    T resolve(V item);

}
