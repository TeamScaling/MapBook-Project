package com.scaling.libraryservice.logging.logger;

import static com.scaling.libraryservice.logging.logger.TaskType.MAP_BOOK_TASK;

import com.scaling.libraryservice.logging.util.LogFormatter;
import com.scaling.libraryservice.logging.util.SlackReporter;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookWrapper;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MapBookLogger extends AbstractLogger<RespMapBookWrapper>{

    public MapBookLogger(SlackReporter slackReporter) {
        super(slackReporter);
    }

    @Override
    TaskType determineTaskType(RespMapBookWrapper value) {
        return MAP_BOOK_TASK;
    }

    @Override
    Map<String, String> collectLogInMap(RespMapBookWrapper wrapper) {
        Map<String, String> logMessageMap = new LinkedHashMap<>();
        logMessageMap.put("title", wrapper.getBookTitle());
        logMessageMap.put("areaCd", String.valueOf(wrapper.getAreaCd()));
        logMessageMap.put("taskTime", wrapper.getTaskTime());
        return logMessageMap;
    }
}
