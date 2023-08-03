package com.scaling.libraryservice.dataPipe.csv.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

@RequiredArgsConstructor
@Slf4j
public class CsvFileMerger {

    private static final String HEADER_NAME = "ISBN,LOAN_CNT";
    private static final String DEFAULT_INPUT_FOLDER = "download\\";

    private static final String DEFAULT_OUTPUT_NAME = "input\\step1.csv";

    public static void main(String[] args) {

        try {
            mergeCsvFile("loanSumFile\\", "mergeFile\\result2.csv",0,1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 분할 된 CSV 파일을 다시 병합
    public static void mergeCsvFile(String inputFolder, String outPutNm,int... recordIdx)
        throws IOException {
        log.info("[CsvFileMerger] is start");
        File[] files = fileLoad(inputFolder);

        AtomicBoolean headerSaved = new AtomicBoolean(false);

        try (BufferedWriter writer = Files.newBufferedWriter(
            Paths.get(outPutNm+".csv"), StandardCharsets.UTF_8)) {

            Arrays.stream(files).forEach(file -> {
                List<String> lines = CsvFileReader.readDataLines(file,recordIdx);
                writeToCsv(writer, file, headerSaved.get(),lines);
                headerSaved.set(true);
            });
        }


        log.info("[CsvFileMerger] is completed");
    }

    private static void writeToCsv(BufferedWriter writer, File file, boolean headerSaved,List<String> lines) {

        try {

            if (!headerSaved) {
                writer.write(HEADER_NAME);
                writer.newLine();
            }

            for(String line : lines){
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String getDefaultInputFolder(){
        return DEFAULT_INPUT_FOLDER;
    }

    public static String getDefaultOutputName(){
        return DEFAULT_OUTPUT_NAME;
    }

    private static String buildCsvLine(String... args) {
        return String.join(",", args);
    }

    private static File[] fileLoad(String inputFolder) {
        return new File(inputFolder).listFiles();
    }

    private static CSVParser getCsvParser(BufferedReader reader) throws IOException {

        return new CSVParser(reader, CSVFormat.DEFAULT);
    }

}