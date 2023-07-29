package com.scaling.libraryservice.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CsvFileMerger {

    public static void mergeCsvFile(String inputFolder, String outputFileName) {

        File[] files = new File(inputFolder)
            .listFiles((dir, name) -> name.endsWith(".csv"));

        boolean headerSaved = false;

        assert files != null;

        try (BufferedWriter writer = Files.newBufferedWriter(
            Paths.get(outputFileName), StandardCharsets.UTF_8)) {

            for (File file : files) {
                List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                // 헤더 처리
                if (!headerSaved) {
                    writeLine(writer,lines.get(0));
                    headerSaved = true;
                }
                // 본문 처리
                lines.stream().skip(1).forEach(line -> writeLine(writer, line));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void writeLine(BufferedWriter writer, String line) {

        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}