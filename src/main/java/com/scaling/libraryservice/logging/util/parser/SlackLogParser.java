package com.scaling.libraryservice.logging.util.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scaling.libraryservice.batch.logTransfer.vo.SlackLogVo;
import com.scaling.libraryservice.logging.logger.TaskType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

// Slack 통해 전달된 로그 파일을 원하느 형태로 파싱

@Component
public class SlackLogParser {
    private static final String COMMON_REGEX = "\\[%s :(.*?)\\]";
    private static final String TASK_CODE_REGEX = "- (\\d+)";
    private static final String DATE_TIME_Regex = "\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\]";

    // 정규 표현식을 이용해서 원하는 상세 메시지를 추출
    public Optional<String> extractMessageFromLog(SlackLogVo slackLogVo,
        String messageRegex) {
        String text = slackLogVo.getText();
        Pattern pattern = Pattern.compile(messageRegex);
        Matcher matcher = pattern.matcher(text);

        return matcher.find() ?
            Optional.of(matcher.group(1).trim())
            : Optional.empty();
    }

    public String getDetailCommonRegex(String target){
        return String.format(COMMON_REGEX,target);
    }
    public String getDateTimeRegex(){
        return DATE_TIME_Regex;
    }
    public String getTaskCodeRegex(){
        return TASK_CODE_REGEX;
    }



}
