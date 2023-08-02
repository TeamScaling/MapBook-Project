package com.scaling.libraryservice.dataPipe.csv.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class CsvLineProvider {

    public static List<String> readDataLines(File file, int... recordIdx) {

        try (
            BufferedReader reader = Files.newBufferedReader(file.toPath());
        ) {
            CSVParser csvParser = getCsvParser(reader);

            return StreamSupport.stream(csvParser.spliterator(), false)
                .map(record -> Arrays.stream(recordIdx)
                    .mapToObj(record::get)
                    .collect(Collectors.joining(","))
                ).toList();

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
