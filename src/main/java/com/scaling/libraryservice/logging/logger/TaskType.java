package com.scaling.libraryservice.logging.logger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskType {

    SLOW_TASK("0001", "slowTask", "#search_log"),
    SEARCH_TASK("0002", "searchTask", "#search_log"),
    NOTFOUND_TASK("0003", "notFoundTask", "#search_log"),

    MAPBOOK_TASK("0004", "ApiErrorTask", "#map_book");


    private final String code;

    private final String name;

    private final String channel;

}