package com.scaling.libraryservice.logging.logger;

import com.scaling.libraryservice.logging.util.LogFormatter;
import com.scaling.libraryservice.logging.util.SlackReporter;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractLogger<V> {

    private final SlackReporter slackReporter;

    public void sendLogToSlack(V value) {
        Map<String, String> logMessages = collectLogInMap(value);

        TaskType taskType = determineTaskType(value);

        slackReporter.report(
                LogFormatter.formatting(taskType, logMessages), taskType.getChannel());
    }

    abstract TaskType determineTaskType(V value);
    
    abstract Map<String, String> collectLogInMap(V value);
}