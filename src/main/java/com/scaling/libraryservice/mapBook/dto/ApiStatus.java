package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.util.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

@Slf4j
public class ApiStatus {

    private final String apiUri;
    private boolean apiAccessible;
    private int errorCnt;
    private DateTime closedTime;
    private DateTime openedTime;
    private DateTime recentClosedTime;

    private final int DEFAULT_MAX_ERROR_CNT;

    public ApiStatus(String apiUri, int DEFAULT_MAX_ERROR_CNT) {
        this.apiUri = apiUri;
        this.DEFAULT_MAX_ERROR_CNT = DEFAULT_MAX_ERROR_CNT;
    }

    public String getApiUri() {
        return apiUri;
    }

    public Integer getErrorCnt() {
        return errorCnt;
    }

    public DateTime getClosedTime() {
        return closedTime;
    }

    public boolean apiAccessible() {
        return apiAccessible;
    }

    public void closeAccess() {
        apiAccessible = false;
        closedTime = DateTime.now();
        log.info("[{}] is not accessible at [{}] ",apiUri,openedTime);
    }

    public void openAccess() {
        apiAccessible = true;
        openedTime = DateTime.now();
        recentClosedTime = closedTime;
        closedTime = null;

        log.info("[{}] is checked for Access at [{}] ",apiUri,openedTime);
    }

    public void upErrorCnt() {
        if (apiAccessible) {
            ++errorCnt;
            if (errorCnt > DEFAULT_MAX_ERROR_CNT) {
                CircuitBreaker.closeObserver(this);
            }
        } else {
            log.info(" service for {} was closed at [{}]",apiUri,closedTime);
        }
    }

}
