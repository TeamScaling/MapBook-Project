package com.scaling.libraryservice.data;

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

    private static final int ISBN_IDX = 5;
    private static final int LOAN_CNT_IDX = 11;
    private static final int REGISTER_DATE_IDX = 12;
    private static final int ISBN_MIN_SIZE = 10;
    private static final String ISBN_REGEX = "^\\d+$";

    public void mergeLibraryData(String inputFolder, String outputFileName) {

        List<LibraryDto> libraries = libraryFindService.getAllLibraries();

        File[] files = getCsvFiles(inputFolder);

        try (BufferedWriter writer = Files.newBufferedWriter(
            Paths.get(outputFileName), StandardCharsets.UTF_8)) {
            processFilesMerging(files, writer, libraries);

        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    private File[] getCsvFiles(String folder) {
        return new File(folder).listFiles((dir, name) -> name.endsWith(".csv"));
    }

    private void processFilesMerging(File[] files, BufferedWriter writer,
        List<LibraryDto> libraries)
        throws IOException {

        boolean headerSaved = false;

        for (File file : files) {

            // 파일명에서 도서관 이름만 추출 한다.
            String libraryName = extractLibraryName(file);

            // 파일에서 추출된 이름이 DB내의 도서관 정보에 일치한 도서관 정보를 찾는다.
            Optional<LibraryDto> libraryOpt =
                libraries.stream()
                    .filter(libray
                        -> libray.getLibNm().contains(libraryName))
                    .findAny();

            if (libraryOpt.isEmpty()) {
                log.info("Library not found: " + libraryName);
            } else {
                String headerNm = "ISBN,LOAN_CNT,LBRRY_CD,REGIS_DATA,AREA_CD";
                LibraryDto library = libraryOpt.get();
                normalizeAndWrite(file, headerSaved, writer, library, headerNm);
            }
        }
    }

    private void normalizeAndWrite(File file, boolean headerSaved, BufferedWriter writer,
        LibraryDto library, String headerName) throws IOException {

        try (Reader reader = Files.newBufferedReader(
            file.toPath(), Charset.forName("EUC-KR"))) {

            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

            if (!headerSaved) {
                writer.write(headerName);
                writer.newLine();
                headerSaved = true;
            }

            for (CSVRecord record : csvParser) {
                normalizeData(writer, record, library);
            }
        }

    }

    // File Name : 구성도서관 장서 대출목록 (2023년 04월)에서  '구성도서관'만 추출
    private String extractLibraryName(File file) {
        return file.getName().split(" ", 3)[0];
    }

    private void normalizeData(BufferedWriter writer, CSVRecord record, LibraryDto library)
        throws IOException {

        String isbn = record.get(ISBN_IDX);
        String loanCount = record.get(LOAN_CNT_IDX);
        String regisDate = record.get(REGISTER_DATE_IDX);

        if (isValidIsbn(isbn)) {
            writer.write(buildCsvLine(isbn, loanCount, library, regisDate));
            writer.newLine();
        } else {
            log.info("Problematic line: " + String.join(",", record));
        }
    }

    private boolean isValidIsbn(String isbn) {
        Pattern pattern = Pattern.compile(ISBN_REGEX);
        Matcher matcher = pattern.matcher(isbn);

        return matcher.matches() && isbn.length() > ISBN_MIN_SIZE;
    }

    private String buildCsvLine(String isbn, String loanCount, LibraryDto library,
        String regisDate) {

        return String.join(",", isbn, loanCount, library.getLibNo().toString(), regisDate,
            library.getAreaCd().toString());
    }


}