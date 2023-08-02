package com.scaling.libraryservice.logging.logger;

import static com.scaling.libraryservice.logging.logger.TaskType.MAP_BOOK_TASK;

import com.scaling.libraryservice.logging.util.SlackReporter;
import com.scaling.libraryservice.mapBook.dto.RespMapBookWrapper;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MapBookLogger extends AbstractLogger<RespMapBookWrapper> {

    public MapBookLogger(SlackReporter slackReporter) {
        super(slackReporter);
    }

    @Override
    TaskType determineTaskType(RespMapBookWrapper value) {
        return MAP_BOOK_TASK;
    }

    @Override
    Map<String, String> collectLogInMap(RespMapBookWrapper wrapper) {
        assert wrapper.getTaskTime() != null;
        return Map.of(
            "title", wrapper.getBookTitle(),
            "areaCd", String.valueOf(wrapper.getAreaCd()),
            "taskTime", wrapper.getTaskTime()
        );
    }
}
