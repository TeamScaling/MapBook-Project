package com.scaling.libraryservice.logging.logger;

import static com.scaling.libraryservice.logging.logger.TaskType.NOTFOUND_TASK;
import static com.scaling.libraryservice.logging.logger.TaskType.NO_LOGGING_TASK;
import static com.scaling.libraryservice.logging.logger.TaskType.SEARCH_TASK;
import static com.scaling.libraryservice.logging.logger.TaskType.SLOW_TASK;

import com.scaling.libraryservice.logging.util.SlackReporter;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SearchSlackLogger extends AbstractSlackLogger<RespBooksDto> {

    private final static double LIMIT_TIME = 1.0;

    private final static int MAX_LOGGING_PAGE = 1;

    public SearchSlackLogger(SlackReporter slackReporter) {
        super(slackReporter);
    }

    @Override
    TaskType determineTaskType(RespBooksDto respBooksDto) {

        // 페이징 처리에 의한 중복 로깅 처리를 방지
        if (isNoLoggingTask(respBooksDto)) {
            return NO_LOGGING_TASK;
        }
        boolean isOverLimitTime = parseDoubleSearchTime(respBooksDto) > LIMIT_TIME;

        return respBooksDto.isEmptyResult() ?
            NOTFOUND_TASK : (isOverLimitTime ? SLOW_TASK : SEARCH_TASK);
    }

    @Override
    Map<String, String> collectLogInMap(RespBooksDto respBooksDto) {
        return Map.of(
            "userQuery", respBooksDto.getMeta().getUserQuery(),
            "searchTime", respBooksDto.getMeta().getSearchTime()
        );
    }

    private boolean isNoLoggingTask(RespBooksDto respBooksDto) {
        return respBooksDto.getMeta().getCurrentPage() > MAX_LOGGING_PAGE;
    }

    private double parseDoubleSearchTime(RespBooksDto respBooksDto) {
        return Double.parseDouble(respBooksDto.getMeta().getSearchTime());
    }

}
