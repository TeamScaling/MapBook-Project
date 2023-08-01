package com.scaling.libraryservice.dataPipe.csv.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
public class CsvFileMerger {
    
    // 분할 된 CSV 파일을 다시 병합
    public static void mergeCsvFile(String inputFolder, String outputFileName, int mergeCnt,
        String fileExtension) {

        File[] files = fileLoad(inputFolder, fileExtension);

        boolean headerSaved = false;

        for (int i = 0; i < files.length; i += mergeCnt) {
            try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(outputFileName + "_" + (i / mergeCnt) + fileExtension),
                StandardCharsets.UTF_8)) {

                for (int j = i; j < i + mergeCnt && j < files.length; j++) {
                    File file = files[j];

                    List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                    // 헤더 처리
                    if (!headerSaved) {
                        writeLine(writer, lines.get(0));
                        headerSaved = true;
                    }
                    // 본문 처리
                    lines.stream().skip(1).forEach(line -> writeLine(writer, line));
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            headerSaved = false;
        }
    }

    private static File[] fileLoad(String inputFolder, String fileExtension) {

        return new File(inputFolder)
            .listFiles((dir, name) -> name.endsWith(fileExtension));
    }

    private static void writeLine(@NonNull BufferedWriter writer, String line) {
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CSVParser getCsvParser(File file,String charsetName,CSVFormat csvFormat) {

        try (Reader reader = Files.newBufferedReader(
            file.toPath(), Charset.forName("EUC-KR"))) {
            return new CSVParser(reader, CSVFormat.DEFAULT);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}