package com.scaling.libraryservice.logging.service;

import com.scaling.libraryservice.logging.exception.IncorrectLogValueException;
import com.scaling.libraryservice.logging.logger.LoggerFactory;
import com.scaling.libraryservice.logging.logger.SlackLogger;
import com.scaling.libraryservice.logging.logger.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogService<V> {

    private final LoggerFactory<V> loggerFactory;

    public void slackLogging(TaskType taskType, V value) {

        SlackLogger<V> logger = loggerFactory.constructSlackLogger(taskType);

        if (isIncorrectValue(taskType, value)) {
            throw new IncorrectLogValueException("This is incorrect Value For Logging system");
        }

        logger.sendLogToSlack(value);
    }


    private boolean isIncorrectValue(TaskType taskType, V value) {
        return value.getClass() != taskType.getTargetClass();
    }
}
