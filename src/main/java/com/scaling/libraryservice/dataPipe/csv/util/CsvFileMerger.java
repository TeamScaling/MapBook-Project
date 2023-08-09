package com.scaling.libraryservice.dataPipe.csv.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

    // 분할 된 CSV 파일을 다시 병합
    public static Path mergeCsvFile(String inputFolder, String outPutNm,String header,int... recordIdx)
        throws IOException {
        log.info("[CsvFileMerger] start");
        File[] files = fileLoad(inputFolder);

        AtomicBoolean headerSaved = new AtomicBoolean(false);

        try (BufferedWriter writer = Files.newBufferedWriter(

            Paths.get(outPutNm+".csv"), StandardCharsets.UTF_8)) {
            Arrays.stream(files).forEach(file -> {
                List<String> lines = CsvFileReader.readDataLines(file,recordIdx);
                writeToCsv(writer, header, headerSaved.get(),lines);
                headerSaved.set(true);
            });
        }

        log.info("[CsvFileMerger] is completed");

        return Path.of(outPutNm);
    }

    private static void writeToCsv(BufferedWriter writer, String header, boolean headerSaved,List<String> lines) {

        try {

            if (header !=null && !headerSaved) {
                writer.write(header);
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