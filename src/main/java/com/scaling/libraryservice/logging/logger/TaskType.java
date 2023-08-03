package com.scaling.libraryservice.logging.logger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskType {

    //느린 검색 작업을 기록
    SLOW_TASK("0001", "slowTask", "#search_log"),

    // 모든 검색 작업을 기록
    SEARCH_TASK("0002", "searchTask", "#search_log"),

    // 실패한 검색 작업을 기록
    NOTFOUND_TASK("0003", "notFoundTask", "#search_log"),

    // 지도 기반 서비스 관련해서 circuitBreaker가 체크하는 에러를 기록.
    API_ERROR_TASK("0004", "ApiErrorTask", "#map_book"),

    ERROR_TASK("0005","serverError","#error_log"),

    MAP_BOOK_TASK("0006","mapbookTask","#map_book"),

    BATCH_TASK("0007","batchTask","#batch_log"),

    NO_LOGGING_TASK("","","");


    private final String code;

    private final String name;

    private final String channel;

}