package com.scaling.libraryservice.logging.logger;

import com.scaling.libraryservice.commons.circuitBreaker.ApiObserver;
import com.scaling.libraryservice.mapBook.dto.RespMapBookWrapper;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskType {

    //느린 검색 작업을 기록
    SLOW_TASK("0001", "slowTask", "#search_log", RespBooksDto.class, SearchSlackLogger.class),

    // 모든 검색 작업을 기록
    SEARCH_TASK("0002", "searchTask", "#search_log",RespBooksDto.class, SearchSlackLogger.class),

    // 실패한 검색 작업을 기록
    NOTFOUND_TASK("0003", "notFoundTask", "#search_log",RespBooksDto.class, SearchSlackLogger.class),

    // 지도 기반 서비스 관련해서 circuitBreaker가 체크하는 에러를 기록.
    API_ERROR_TASK("0004", "ApiErrorTask", "#map_book", ApiObserver.class, OpenApiSlackLogger.class),

    ERROR_TASK("0005","serverError","#error_log", Exception.class, ErrorSlackLogger.class),

    MAP_BOOK_TASK("0006","mapbookTask","#map_book", RespMapBookWrapper.class, MapBookSlackLogger.class),

    BATCH_TASK("0007","batchTask","#batch_log",String.class, BatchSlackLogger.class),

    NO_LOGGING_TASK("","","",null,null);


    private final String code;

    private final String name;

    private final String channel;

    private final Class<?> targetClass;

    private final Class<? extends SlackLogger<?>> slackLogger;

}