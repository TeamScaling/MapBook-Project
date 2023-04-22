package com.scaling.libraryservice.mapBook.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Location {

    private final double lat;

    private final double lon;

}
