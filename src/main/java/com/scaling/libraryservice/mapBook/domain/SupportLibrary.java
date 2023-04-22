package com.scaling.libraryservice.mapBook.domain;

import java.util.Arrays;

public enum SupportLibrary {

    SeongnamSi(26200);

    private final int areaCd;

    SupportLibrary(int areaCd) {
        this.areaCd = areaCd;
    }

    public int getAreaCd() {
        return areaCd;
    }

    public boolean isSupport(int areaCd){

        return false;
    }

}
