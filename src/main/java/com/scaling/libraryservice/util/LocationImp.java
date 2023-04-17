package com.scaling.libraryservice.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocationImp implements Location{

    private final double lat;

    private final double lon;

    @Override
    public double getLat() {
        return this.lat;
    }

    @Override
    public double getLon() {
        return this.lon;
    }

}
