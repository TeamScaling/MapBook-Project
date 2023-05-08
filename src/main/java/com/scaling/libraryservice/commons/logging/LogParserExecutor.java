package com.scaling.libraryservice.commons.logging;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.Setter;


public class LogParserExecutor {

    public static void main(String[] args) {
        String inputNm = "test_book.json"; // 로그 파일 이름을 입력합니다.
        String outputNm = "test_book_result.json";

        executeParse(inputNm, outputNm);
    }

    @Getter
    @Setter
    static class LogData {

        String query;
        String titleQuery;
        String duration;

        public LogData() {
        }

        public LogData(String query, String titleQuery, String duration) {
            this.query = query;
            this.titleQuery = titleQuery;
            this.duration = duration;
        }


        @Override
        public String toString() {
            return "LogData{" +
                "query='" + query + '\'' +
                ", titleQuery='" + titleQuery + '\'' +
                ", duration=" + duration +
                '}';
        }
    }

    private static void executeParse(String inputFile, String outputFile) {

        Gson gson = new Gson();

        try {

            List<LogData> logDataList = parseLogData(inputFile);

            var result = gson.toJson(logDataList);

            FileWriter fileWriter = new FileWriter(outputFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            logDataList.stream().forEach(l -> {
                try {
                    if (l.getDuration() != null) {
                        bufferedWriter.write(l.getDuration() + "\n");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<LogData> parseLogData(String fileName) throws IOException {
        List<LogData> logDataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            String query = "";
            String titleQuery = "";
            String duration = "";

            Pattern queryPattern = Pattern.compile("query : \\[(.+?)\\]");
            Pattern titleQueryPattern = Pattern.compile("TitleQuery\\((.+?)\\)");
            Pattern durationPattern = Pattern.compile("\\[(\\d+\\.\\d+)s\\]");
            Pattern classPattern = Pattern.compile(
                "\\[com\\.scaling\\.libraryservice\\.search\\.service\\.BookSearchService\\]");

            while ((line = br.readLine()) != null) {
                Matcher queryMatcher = queryPattern.matcher(line);
                Matcher titleQueryMatcher = titleQueryPattern.matcher(line);
                Matcher durationMatcher = durationPattern.matcher(line);
                Matcher classMatcher = classPattern.matcher(line);

                LogData logData = new LogData();

                if (queryMatcher.find()) {
                    query = queryMatcher.group(1);
                    logData.setQuery(query);
                }
                if (titleQueryMatcher.find()) {
                    titleQuery = titleQueryMatcher.group(1);

                    logData.setTitleQuery(titleQuery);
                }

                if (classMatcher.find()) {
                    if (durationMatcher.find()) {

                        duration = durationMatcher.group(1);

                            logData.setDuration(duration);
                    }
                }
                logDataList.add(logData);
            }
        }

        return logDataList;
    }

}



