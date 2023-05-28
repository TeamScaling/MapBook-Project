package com.scaling.libraryservice.commons.circuitBreaker;

import com.scaling.libraryservice.mapBook.domain.ApiConnection;
import com.scaling.libraryservice.mapBook.domain.ApiObserver;

public interface RestorationChecker {

    boolean isRestoration(ApiConnection apiConnection);
}
