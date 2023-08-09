package com.scaling.libraryservice.logging.logger;

import com.scaling.libraryservice.logging.exception.NotFoundLoggerException;
import com.scaling.libraryservice.logging.logger.SlackLogger;
import com.scaling.libraryservice.logging.logger.TaskType;
import com.scaling.libraryservice.logging.util.SlackReporter;
import java.lang.reflect.Constructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoggerFactory<V> {

    private final SlackReporter slackReporter;

    @SuppressWarnings("unchecked")
    public SlackLogger<V> constructSlackLogger(TaskType taskType) {

        try {
            return (SlackLogger<V>) getLoggerConstructor(taskType).newInstance(slackReporter);
        } catch (Exception e) {
            throw new NotFoundLoggerException();
        }
    }

    private Constructor<? extends SlackLogger<?>> getLoggerConstructor(TaskType taskType)
        throws NoSuchMethodException {

        return taskType.getSlackLogger().getConstructor(SlackReporter.class);
    }

}
