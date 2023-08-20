package com.scaling.libraryservice.logging.util;

import com.scaling.libraryservice.logging.logger.TaskType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

//로그 메시지를 구조화 하기 위한 클래스
public class LogFormatter {

    // Logger 클래스가 최종적으로 사용하는 메소드. TaskType은 해당 로그가 어떤 작업인지를 나타낸다
    // logMessageMap엔 logger가 저장하는 [userQuery (key) : 정석(value)] 식의 로그 메시지이다.
    public static String formatting(TaskType taskType, Map<String,String> logMessageMap){

        return createLogTemplate(taskType)
            .append(formatLogMessages(logMessageMap))
            .toString();
    }

    // 공통적인 로그 구조를 StringBuilder에 넣어서 반환 한다.
    private static StringBuilder createLogTemplate(TaskType taskType) {
        return new StringBuilder(formatBasicLog(taskType));
    }

    // 공통적인 로그 구조를 나타 낸다.
    private static String formatBasicLog(TaskType taskType) {
        return String.format(
            "[%s] - %s [%s] --- ",
            formatDateTime(LocalDateTime.now()),
            taskType.getCode(),
            taskType.getName()
        );
    }

    // 공통적으로 로그 발생 날짜 시간을 위한 구조
    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    // 상세 로그 메시지에 대한 구조를 처리 한다.
    private static String formatLogMessages(Map<String, String> logMessages) {

        return logMessages.entrySet().stream()
            .map(entry -> String.format("[%s : %s]", entry.getKey(),entry.getValue()))
            .collect(Collectors.joining(" "));
    }

}
