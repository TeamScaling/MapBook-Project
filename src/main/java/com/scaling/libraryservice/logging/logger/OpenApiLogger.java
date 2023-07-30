package com.scaling.libraryservice.logging.logger;

import static com.scaling.libraryservice.logging.logger.TaskType.API_ERROR_TASK;

import com.scaling.libraryservice.commons.circuitBreaker.ApiStatus;
import com.scaling.libraryservice.logging.util.LogFormatter;
import com.scaling.libraryservice.logging.util.SlackReporter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OpenApiLogger extends AbstractLogger<ApiStatus> {

    public OpenApiLogger(SlackReporter slackReporter) {
        super(slackReporter);
    }

    @Override
    Map<String, String> collectLogInMap(ApiStatus status) {
        Map<String, String> logMessageMap = new LinkedHashMap<>();
        logMessageMap.put("apiUrl", status.getApiUri());
        logMessageMap.put("closedTime", LogFormatter.formatDateTime(status.getClosedTime()));
        return logMessageMap;
    }

    @Override
    TaskType determineTaskType(ApiStatus status) {
        return API_ERROR_TASK;
    }

}
