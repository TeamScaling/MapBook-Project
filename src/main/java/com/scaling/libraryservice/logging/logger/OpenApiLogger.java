package com.scaling.libraryservice.logging.logger;

import static com.scaling.libraryservice.logging.logger.TaskType.API_ERROR_TASK;

import com.scaling.libraryservice.commons.circuitBreaker.ApiStatus;
import com.scaling.libraryservice.logging.util.LogFormatter;
import com.scaling.libraryservice.logging.util.SlackReporter;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OpenApiLogger extends AbstractLogger<ApiStatus> {

    public OpenApiLogger(SlackReporter slackReporter) {
        super(slackReporter);
    }

    @Override
    Map<String, String> collectLogInMap(ApiStatus status) {
        return Map.of(
            "apiUrl", status.getApiUri(),
            "closedTime", LogFormatter.formatDateTime(status.getClosedTime())
        );
    }

    @Override
    TaskType determineTaskType(ApiStatus status) {
        return API_ERROR_TASK;
    }

}
