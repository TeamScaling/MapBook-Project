package com.scaling.libraryservice.dataPipe.libraryCatalog;

import com.scaling.libraryservice.dataPipe.aop.BatchLogging;
import com.scaling.libraryservice.mapBook.dto.LibraryInfoDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
public class LibraryCatalogNormalizer {

    private static final int ISBN_IDX = 5;
    private static final int LOAN_CNT_IDX = 11;
    private static final int ISBN_MIN_SIZE = 10;
    private static final String ISBN_REGEX = "^\\d+$";

    private static final String LOAN_CNT_REGEX = "^-?\\d+$";

    public static Path normalize(String inputFolder,String outPutFolder) {
        File[] files = getCsvFiles(inputFolder);
        processNormalize(files,outPutFolder);
        return Path.of(inputFolder);
    }

    private static void processNormalize(File[] files,String outPutFolder) {
        Arrays.stream(files)
            .parallel()
            .forEach(file -> {
                Path outputPath = Path.of(outPutFolder+"/"+file.getName().split(" ")[0]+ "normalized.csv");
                try {
                    normalizeAndWrite(file, outputPath);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
    }

    private static void normalizeAndWrite(File file, Path outputPath) throws IOException {

        List<String> allLines = Files.readAllLines(file.toPath(), Charset.forName("EUC-KR"));

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {

            allLines.parallelStream()
                .map(LibraryCatalogNormalizer::extractTarget)
                .filter(line -> !line.isBlank())
                .forEach(line -> {
                    try {
                        writer.write(line);
                        writer.newLine();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
        }
    }

    private static String extractTarget(String line) {

        StringJoiner joiner = new StringJoiner(",");
        String[] split = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        if (split.length > LOAN_CNT_IDX) {
            String isbnValue = split[ISBN_IDX].replace("\"", "");
            String loanCntValue = split[LOAN_CNT_IDX].replace("\"", "");

            if (isValidIsbn(isbnValue) && isValidLoanCnt(loanCntValue)) {
                joiner.add(isbnValue);
                joiner.add(loanCntValue);
            }
        }
        return joiner.toString();
    }

    private static File[] getCsvFiles(String folder) {
        return new File(folder).listFiles((dir, name) -> name.endsWith(".csv"));
    }

    private static boolean isValidIsbn(String isbn) {
        Pattern pattern = Pattern.compile(ISBN_REGEX);
        Matcher matcher = pattern.matcher(isbn);
        return matcher.matches() && isbn.length() > ISBN_MIN_SIZE;
    }

    private static boolean isValidLoanCnt(String loanCnt) {
        Pattern pattern = Pattern.compile(LOAN_CNT_REGEX);
        Matcher matcher = pattern.matcher(loanCnt);

        return matcher.matches();
    }

}