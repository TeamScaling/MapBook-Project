package com.scaling.libraryservice.dataPipe.libraryCatalog;

import com.scaling.libraryservice.dataPipe.aop.BatchLogging;
import com.scaling.libraryservice.mapBook.dto.LibraryInfoDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    //    @BatchLogging
    public static Path normalize(String inputFolder, String outputFileName) {

        File[] files = getCsvFiles(inputFolder);

        Path outPutPath = Path.of(outputFileName);

        try (BufferedWriter writer = Files.newBufferedWriter(
            outPutPath, StandardCharsets.UTF_8)) {
            processNormalize(files, writer);

            return outPutPath;

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static File[] getCsvFiles(String folder) {
        return new File(folder).listFiles((dir, name) -> name.endsWith(".csv"));
    }

    private static void processNormalize(File[] files, BufferedWriter writer)
        throws IOException {

        AtomicBoolean headerSaved = new AtomicBoolean(false);

        Arrays.stream(files)
            .forEach(file -> {

                String headerNm = "ISBN,LOAN_CNT";
                normalizeAndWrite(file, headerSaved, writer, headerNm);
            });
    }

    private static void normalizeAndWrite(
        File file, AtomicBoolean headerSaved, BufferedWriter writer, String headerName) {

        try (Reader reader = Files.newBufferedReader(
            file.toPath(), Charset.forName("EUC-KR"))) {

            CSVParser csvParser = CSVFormat.DEFAULT
                .parse(reader);

            if (!headerSaved.get()) {
                writer.write(headerName);
                writer.newLine();
                headerSaved.set(true);
            }

            for (CSVRecord record : csvParser) {
                normalizeData(writer, record);
            }


        } catch (IOException e) {
            System.out.println("skip");
        }
    }



    private static void normalizeData(BufferedWriter writer, CSVRecord record)
         {

        String isbn = record.get(ISBN_IDX);
        String loanCount = record.get(LOAN_CNT_IDX);

        if (isValidIsbn(isbn)) {
            try {
                writer.write(buildCsvLine(isbn, loanCount));
                writer.newLine();
            } catch (IOException e) {
                System.out.println("Error on line: " + record.getRecordNumber());
                System.out.println("Line content: " + record.toString());
            }

        }
    }

    private static boolean isValidIsbn(String isbn) {
        Pattern pattern = Pattern.compile(ISBN_REGEX);
        Matcher matcher = pattern.matcher(isbn);

        return matcher.matches() && isbn.length() > ISBN_MIN_SIZE;
    }

    private static String buildCsvLine(String isbn, String loanCount) {
        return String.join(",", isbn, loanCount);
    }
}