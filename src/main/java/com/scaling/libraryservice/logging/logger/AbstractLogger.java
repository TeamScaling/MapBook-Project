package com.scaling.libraryservice.logging.logger;

import static com.scaling.libraryservice.logging.logger.TaskType.NO_LOGGING_TASK;

import com.scaling.libraryservice.logging.util.LogFormatter;
import com.scaling.libraryservice.logging.util.SlackReporter;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractLogger<V> {

    private final SlackReporter slackReporter;

    // logger 구현체가 공통으로 사용하는 메소드. slackReporter를 통해 실제로 메시지 전송
    public void sendLogToSlack(V value) {
        Map<String, String> logMessages = collectLogInMap(value);

        TaskType taskType = determineTaskType(value);

        // logging이 필요 없는 task면 로깅 메시지를 보내지 않는다.
        if(taskType != NO_LOGGING_TASK){
            String message = LogFormatter.formatting(taskType, logMessages);
            slackReporter.report(message, taskType.getChannel());
        }
    }

    //구현체마다 원하는 TaskType을 결정 해야한다.
    abstract TaskType determineTaskType(V value);

    // 로그 상세 메시지를 map에 구조화 한다. 구현체마다 구현 해야한다.
    abstract Map<String, String> collectLogInMap(V value);
}