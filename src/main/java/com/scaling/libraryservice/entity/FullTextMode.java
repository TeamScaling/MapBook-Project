package com.scaling.libraryservice.entity;

public enum FullTextMode {

    NATURE("IN NATURAL LANGUAGE MODE"),BOOLEAN("IN BOOLEAN MODE");

    private final String mode;

    FullTextMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
