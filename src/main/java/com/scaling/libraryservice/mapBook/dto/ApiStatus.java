package com.scaling.libraryservice.mapBook.dto;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/**
 *  OpenAPI에 대한 연결 상태 정보를 담는다. 연결에 문제가 있을 시, 에러 정보를 저장 한다.
 */
@Slf4j @Getter
public class ApiStatus {

    private final String apiUri;
    private boolean apiAccessible;
    private int errorCnt;
    private DateTime closedTime;
    private DateTime openedTime;
    private DateTime recentClosedTime;
    public final int DEFAULT_MAX_ERROR_CNT;

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
        errorCnt = 0;

        log.info("[{}] is checked for Access at [{}] ",apiUri,openedTime);
    }

    public void upErrorCnt() {
        ++errorCnt;
    }

}
