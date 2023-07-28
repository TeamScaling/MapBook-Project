package com.scaling.libraryservice.logging.util;

import com.scaling.libraryservice.logging.logger.TaskType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class LogFormatter {
    public static String formatting(TaskType taskType, Map<String,String> map){

        return createLogTemplate(taskType)
            .append(formatLogMessages(map)).toString();
    }

    private static StringBuilder createLogTemplate(TaskType taskType) {
        return new StringBuilder(formatBasicLog(taskType));
    }

    private static String formatBasicLog(TaskType taskType) {
        return String.format(
            "[%s] - %s [%s] --- ",
            formatDateTime(LocalDateTime.now()),
            taskType.getCode(),
            taskType.getName());
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    private static String formatLogMessages(Map<String, String> logMessages) {

        return logMessages.entrySet().stream()
            .map(entry -> String.format("[%s : %s]", entry.getKey(), entry.getValue()))
            .collect(Collectors.joining(" "));
    }

}
