package com.scaling.libraryservice.logging.logger;

import com.scaling.libraryservice.logging.util.SlackReporter;
import java.util.Arrays;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ErrorSlackLogger extends AbstractSlackLogger<Exception> {

    public ErrorSlackLogger(SlackReporter slackReporter) {
        super(slackReporter);
    }

    @Override
    TaskType determineTaskType(Exception value) {
        return TaskType.ERROR_TASK;
    }

    @Override
    Map<String, String> collectLogInMap(Exception value) {
        return Map.of(
            "class", value.toString(),
            "stackTrace", Arrays.toString(value.getStackTrace())
        );
    }
}
