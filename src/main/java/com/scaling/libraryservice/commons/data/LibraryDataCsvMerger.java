package com.scaling.libraryservice.commons.data;

import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
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
@Component
public class LibraryDataCsvMerger {

    private final LibraryFindService libraryFindService;

    public void mergeLibraryData(String inputFolder, String outputFileName) {

        List<LibraryDto> libraries = libraryFindService.getAllLibraries();

        File[] files = getCsvFiles(inputFolder);

        try (BufferedWriter writer = Files.newBufferedWriter(
            Paths.get(outputFileName), StandardCharsets.UTF_8)) {

            processFiles(files, writer, libraries);

        } catch (IOException e) {
            log.info(e + "");
        }
    }

    private File[] getCsvFiles(String folder) {
        return new File(folder).listFiles((dir, name) -> name.endsWith(".csv"));
    }

    private void processFiles(File[] files, BufferedWriter writer, List<LibraryDto> libraries)
        throws IOException {

        boolean headerSaved = false;

        for (File file : files) {

            String libraryName = extractLibraryName(file);

            Optional<LibraryDto> libraryOpt =
                libraries.stream().filter(l -> l.getLibNm().contains(libraryName)).findAny();

            if (libraryOpt.isEmpty()) {
                log.info("Library not found: " + libraryName);

            } else {
                LibraryDto library = libraryOpt.get();

                try (Reader reader = Files.newBufferedReader(
                    file.toPath(), Charset.forName("EUC-KR"))) {

                    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

                    if (!headerSaved) {
                        writer.write("ISBN,LOAN_CNT,LBRRY_CD,REGIS_DATA,AREA_CD");
                        writer.newLine();
                        headerSaved = true;
                    }

                    for (CSVRecord record : csvParser) {
                        normalizeData(writer, record, library);
                    }
                }
            }
        }
    }

    // File Name : 구성도서관 장서 대출목록 (2023년 04월)에서  '구성도서관'만 추출
    private String extractLibraryName(File file) {
        return file.getName().split(" ", 3)[0];
    }

    private void normalizeData(BufferedWriter writer, CSVRecord record, LibraryDto library)
        throws IOException {

        String isbn = record.get(5);
        String loanCount = record.get(11);
        String regisDate = record.get(12);

        if (isValidIsbn(isbn)) {
            writer.write(buildCsvLine(isbn, loanCount, library, regisDate));
            writer.newLine();
        } else {
            log.info("Problematic line: " + String.join(",", record));
        }
    }

    private boolean isValidIsbn(String isbn) {
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(isbn);

        return matcher.matches() && isbn.length() > 10;
    }

    private String buildCsvLine(String isbn, String loanCount, LibraryDto library,
        String regisDate) {

        return String.join(",", isbn, loanCount, library.getLibNo().toString(), regisDate,
            library.getAreaCd().toString());
    }


}