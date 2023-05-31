package com.scaling.libraryservice.commons.circuitBreaker;

public interface RestorationChecker {

    boolean isRestoration(ApiObserver observer);
}
