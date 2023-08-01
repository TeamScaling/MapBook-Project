package com.scaling.libraryservice.logging.logger;

import static com.scaling.libraryservice.logging.logger.TaskType.NOTFOUND_TASK;
import static com.scaling.libraryservice.logging.logger.TaskType.NO_LOGGING_TASK;
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

    private final static int MAX_LOGGING_PAGE = 1;

    public SearchLogger(SlackReporter slackReporter) {
        super(slackReporter);
    }

    @Override
    TaskType determineTaskType(RespBooksDto respBooksDto) {
        
        // 페이징 처리에 의한 중복 로킹 처리를 방지
        if (isNoLoggingTask(respBooksDto)) {
            return NO_LOGGING_TASK;
        }

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

    private boolean isNoLoggingTask(RespBooksDto respBooksDto) {
        return respBooksDto.getMeta().getCurrentPage() > MAX_LOGGING_PAGE;
    }

    private double parseDoubleSearchTime(RespBooksDto respBooksDto) {
        return Double.parseDouble(respBooksDto.getMeta().getSearchTime());
    }

}
