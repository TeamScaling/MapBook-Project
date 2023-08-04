package com.scaling.libraryservice.logging.logger;

import com.scaling.libraryservice.logging.util.SlackReporter;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BatchSlackLogger extends AbstractSlackLogger<String> {

    public BatchSlackLogger(SlackReporter slackReporter) {
        super(slackReporter);
    }

    @Override
    TaskType determineTaskType(String value) {
        return TaskType.BATCH_TASK;
    }

    @Override
    Map<String, String> collectLogInMap(String value) {
        return Map.of(
            "logMessage", value
        );
    }
}
