package com.scaling.libraryservice.logging.logger;

import com.scaling.libraryservice.logging.util.SlackReporter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ErrorLogger extends AbstractLogger<Exception>{

    public ErrorLogger(SlackReporter slackReporter) {
        super(slackReporter);
    }

    @Override
    TaskType determineTaskType(Exception value) {
        return TaskType.ERROR_TASK;
    }

    @Override
    Map<String, String> collectLogInMap(Exception value) {
        Map<String, String> logMessageMap = new LinkedHashMap<>();
        logMessageMap.put("class", value.toString());
        logMessageMap.put("stackTrace", Arrays.toString(value.getStackTrace()));
        return logMessageMap;
    }
}
