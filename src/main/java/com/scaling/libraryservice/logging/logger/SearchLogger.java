package com.scaling.libraryservice.logging.logger;

import static com.scaling.libraryservice.logging.logger.TaskType.NOTFOUND_TASK;
import static com.scaling.libraryservice.logging.logger.TaskType.SEARCH_TASK;
import static com.scaling.libraryservice.logging.logger.TaskType.SLOW_TASK;

import com.scaling.libraryservice.logging.util.SlackReporter;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SearchLogger extends AbstractLogger<RespBooksDto> {

    private final static double LIMIT_TIME = 1.0;

    public SearchLogger(SlackReporter slackReporter) {
        super(slackReporter);
    }

    @Override
    TaskType determineTaskType(RespBooksDto respBooksDto) {
        return respBooksDto.isEmptyResult() ? NOTFOUND_TASK
            : (parseDoubleSearchTime(respBooksDto) > LIMIT_TIME ? SLOW_TASK : SEARCH_TASK);
    }

    @Override
    Map<String, String> collectLogInMap(RespBooksDto respBooksDto) {
        Map<String, String> logMessageMap = new LinkedHashMap<>();
        logMessageMap.put("userQuery", respBooksDto.getMeta().getUserQuery());
        logMessageMap.put("searchTime", respBooksDto.getMeta().getSearchTime());
        return logMessageMap;
    }

    private double parseDoubleSearchTime(RespBooksDto respBooksDto) {
        return Double.parseDouble(respBooksDto.getMeta().getSearchTime());
    }

}
