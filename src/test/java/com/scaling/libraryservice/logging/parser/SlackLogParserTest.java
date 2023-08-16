package com.scaling.libraryservice.logging.parser;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.logging.logger.TaskType;
import java.util.Comparator;
import java.util.List;

class SlackLogParserTest {

    public static void main(String[] args) {

        List<String> list = SlackLogParser.parsingLogData("logs", TaskType.SLOW_TASK,
            SlackLogParser.getSearchTimeRegex());

        list.sort(Comparator.comparingDouble(Double::parseDouble).reversed());

        list.forEach(System.out::println);
        System.out.println(list.size());

    }

}