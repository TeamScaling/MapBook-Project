package com.scaling.libraryservice.logging.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scaling.libraryservice.logging.vo.SlackLogVo;
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

// Slack 통해 전달된 로그 파일을 원하느 형태로 파싱
public class SlackLogParser {
    private static final String USER_QUERY_REGEX = "\\[userQuery :(.*?)\\]";
    private static final String MAP_BOOK_TITLE = "\\[title :(.*?)\\]";
    private static final String DEFAULT_FILE_EXTENSION = ".json";

    // 예시 [2023-08-01 11:48:38] - 0002 [searchTask] --- [userQuery : 아메리칸 프로메테우스] [searchTime : 0.008]

    // 로그 파싱 실행 메소드
    public static List<String> parsingLogData(String inputFolder,
        TaskType taskType, String messageRegex) {

        File[] files = filterFilesByExtension(inputFolder);

        return Arrays.stream(files)
            .flatMap(
                file ->
                    filterLogsByTaskType(
                        taskType.getName(), readLogDataFromFile(file), messageRegex).stream()
            )
            .collect(Collectors.toList());
    }

    // 로그 메시지를 쉽게 찾기 위한 helper
    public static String getUserQueryRegex(){
        return USER_QUERY_REGEX;
    }
    // 로그 메시지를 쉽게 찾기 위한 helper
    public static String getMapBookTitle(){
        return MAP_BOOK_TITLE;
    }
    
    // log 파일에서 객체로 변환 된 log 데이터에서 원하는 Task을 찾는다 ex) slowTask - 3초 이상의 느린 검색
    private static List<String> filterLogsByTaskType(String targetTask, List<SlackLogVo> logList,
        String messageRegex) {

        return logList.stream()
            .filter(slackLogVo -> slackLogVo.getText().contains(targetTask))
            .map(slackLogVo -> extractMessageFromLog(slackLogVo, messageRegex))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }
    
    // 정규 표현식을 이용해서 원하는 상세 메시지를 추출
    private static Optional<String> extractMessageFromLog(SlackLogVo slackLogVo, String messageRegex) {
        String text = slackLogVo.getText();
        Pattern pattern = Pattern.compile(messageRegex);
        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? Optional.of(matcher.group(1).trim()) : Optional.empty();
    }

    // 파일의 로그 데이터를 객체로 변환 한다.
    private static List<SlackLogVo> readLogDataFromFile(File file) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Type logTypeListType = new TypeToken<List<SlackLogVo>>() {}.getType();
            return new Gson().fromJson(reader, logTypeListType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static File[] filterFilesByExtension(String inputFolder) {
        return new File(inputFolder)
            .listFiles((dir, name) -> name.endsWith(DEFAULT_FILE_EXTENSION));
    }

}
