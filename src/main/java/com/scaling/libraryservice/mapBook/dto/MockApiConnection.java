package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.domain.ApiObservable;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import com.scaling.libraryservice.mapBook.util.CircuitBreaker;
import org.joda.time.DateTime;
import org.springframework.web.util.UriComponentsBuilder;

public class MockApiConnection implements ApiObservable, ConfigureUriBuilder {

    private static boolean apiAccessible = true;
    private static Integer errorCnt = 0;
    private static DateTime closedTime = null;
    private static DateTime openedTime = null;

    private static DateTime recentClosedTime = null;

    @Override
    public UriComponentsBuilder configUriBuilder(String target) {
        return UriComponentsBuilder.fromHttpUrl("http://localhost:" + 8089 + "/api/bookExist");
    }

    @Override
    public String getApiUrl() {
        return "http://localhost:" + 8089 + "/api/bookExist";
    }

    @Override
    public Integer getErrorCnt() {
        return errorCnt;
    }

    @Override
    public DateTime getClosedTime() {
        return closedTime;
    }

    @Override
    public boolean apiAccessible() {
        return apiAccessible;
    }

    @Override
    public void closeAccess() {
        apiAccessible = false;
        closedTime = DateTime.now();
    }

    @Override
    public void openAccess() {
        apiAccessible = true;
        openedTime = DateTime.now();
        recentClosedTime = closedTime;
        closedTime = null;
    }

    @Override
    public void upErrorCnt() {
        ++errorCnt;

        if(errorCnt > 5){
            CircuitBreaker.closeObserver(this);
        }
    }
}
