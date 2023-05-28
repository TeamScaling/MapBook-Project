package com.scaling.libraryservice.commons.circuitBreaker;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * OpenAPI에 대한 연결 상태 정보를 담는다. 연결에 문제가 있을 시, 에러 정보를 저장 한다.
 */
@Slf4j
@Getter
public class ApiStatus {

    private final String apiUri;
    private boolean apiAccessible = true;
    private int errorCnt;
    private long tryCnt = 1;

    private LocalDateTime closedTime;
    private LocalDateTime openedTime;
    private LocalDateTime recentClosedTime;
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

    public LocalDateTime getClosedTime() {
        return closedTime;
    }

    public boolean apiAccessible() {
        return apiAccessible;
    }

    void closeAccess() {
        apiAccessible = false;
        closedTime = LocalDateTime.now();
        log.info("[{}] is not accessible at [{}] ", apiUri, openedTime);
    }

    void openAccess() {
        apiAccessible = true;
        openedTime = LocalDateTime.now();
        recentClosedTime = closedTime;
        closedTime = null;
        errorCnt = 0;

        log.info("[{}] is checked for Access at [{}] ", apiUri, openedTime);
    }

    boolean upErrorCnt() {

        return ++errorCnt > DEFAULT_MAX_ERROR_CNT;
    }

    void upTryCnt() {
        ++tryCnt;
    }
}
