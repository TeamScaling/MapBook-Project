package com.scaling.libraryservice.commons.logging;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;


public class LogParser {

    public static void main(String[] args) {
        String fileName = "test_book_log.log"; // 로그 파일 이름을 입력합니다.

        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();

        try {
            /*List<LogData> logDataList = parseLogData(fileName).stream().filter(l -> l.duration >= 1.0)
                .sorted(new Comparator<LogData>() {
                    @Override
                    public int compare(LogData o1, LogData o2) {
                        if(o1.duration > o2.duration) return -1;
                        else if (o1.duration<o2.duration) {
                            return 1;
                        }else return 0;
                    }
                }).toList();*/

            List<LogData3> logDataList = parseLogData(fileName);

            System.out.println(logDataList.size());

            var result = gson.toJson(logDataList);

            FileWriter fileWriter = new FileWriter("test_book_log_json_duration1.json");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (LogData3 l : logDataList){
                bufferedWriter.write( l.duration+"\n");
            }

            /*bufferedWriter.write(result);*/


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class LogData {
        String query;
        String titleQuery;
        double duration;

        public LogData(String query, String titleQuery, double duration) {
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

    static class LogData2 {
        double duration;

        public LogData2(double duration) {

            this.duration = duration;
        }

        @Override
        public String toString() {
            return "LogData2{" +
                "duration=" + duration +
                '}';
        }
    }

    static class LogData3 {
        String query;
        double duration;

        public LogData3(String query, double duration) {
            this.query = query;
            this.duration = duration;
        }

        @Override
        public String toString() {
            return "LogData3{" +
                "query='" + query + '\'' +
                ", duration=" + duration +
                '}';
        }
    }

    private static List<LogData3> parseLogData(String fileName) throws IOException {
        List<LogData3> logDataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            String query = "";
            String titleQuery = "";
            double duration = 0;

            Pattern queryPattern = Pattern.compile("-+query : \\[(.+?)\\]-+");
            /*Pattern titleQueryPattern = Pattern.compile("TitleQuery\\((.+?)\\)");*/
            Pattern durationPattern = Pattern.compile("\\[(\\d+\\.\\d+)s\\]");

            while ((line = br.readLine()) != null) {
                Matcher queryMatcher = queryPattern.matcher(line);
                /*Matcher titleQueryMatcher = titleQueryPattern.matcher(line);*/
                Matcher durationMatcher = durationPattern.matcher(line);

                /*if (durationMatcher.find()){
                    duration = Double.parseDouble(durationMatcher.group(1));
                    logDataList.add(new LogData2(duration));
                }*/

                if (queryMatcher.find()) {
                    query = queryMatcher.group(1);
                } /*else if (titleQueryMatcher.find()) {
                    titleQuery = titleQueryMatcher.group(1);
                } */else if (durationMatcher.find()) {
                    duration = Double.parseDouble(durationMatcher.group(1));
                    logDataList.add(new LogData3(query, duration));
                }
            }
        }

        return logDataList;
    }
}



