package com.scaling.libraryservice.logging.logger;

import static com.scaling.libraryservice.logging.logger.TaskType.MAP_BOOK_TASK;
import static com.scaling.libraryservice.logging.logger.TaskType.NO_LOGGING_TASK;

import com.scaling.libraryservice.logging.util.SlackReporter;
import com.scaling.libraryservice.mapBook.dto.RespMapBookWrapper;
import java.util.Collections;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MapBookSlackLogger extends AbstractSlackLogger<RespMapBookWrapper> {

    public MapBookSlackLogger(SlackReporter slackReporter) {
        super(slackReporter);
    }

    @Override
    TaskType determineTaskType(RespMapBookWrapper value) {

        if (value.getBookTitle() == null){
            return NO_LOGGING_TASK;
        }

        return MAP_BOOK_TASK;
    }

    @Override
    Map<String, String> collectLogInMap(RespMapBookWrapper wrapper) {
        assert wrapper.getTaskTime() != null;

        if(wrapper.getBookTitle() == null){
            return Collections.emptyMap();
        }

        return Map.of(
            "title", wrapper.getBookTitle(),
            "areaCd", String.valueOf(wrapper.getAreaCd()),
            "taskTime", wrapper.getTaskTime()
        );
    }
}
