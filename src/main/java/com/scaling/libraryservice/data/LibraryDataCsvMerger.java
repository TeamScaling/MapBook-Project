package com.scaling.libraryservice.data;

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
@Component
public class LibraryDataCsvMerger {

    private final LibraryFindService libraryFindService;

    private static final int ISBN_IDX = 5;
    private static final int LOAN_CNT_IDX = 11;
    private static final int REGISTER_DATE_IDX = 12;
    private static final int ISBN_MIN_SIZE = 10;
    private static final String ISBN_REGEX = "^\\d+$";

    private static final String HEADER_NAME = "ISBN,LOAN_CNT,LBRRY_CD,REGIS_DATA,AREA_CD";

    public void mergeLibraryData(String inputFolder, String outputFileName) {

        List<LibraryInfoDto> libraries = libraryFindService.getAllLibraries();

        File[] files = getCsvFiles(inputFolder);

        try (BufferedWriter writer = Files.newBufferedWriter(
            Paths.get(outputFileName), StandardCharsets.UTF_8)) {
            processFilesMerging(files, writer, libraries);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private File[] getCsvFiles(String folder) {
        return new File(folder).listFiles((dir, name) -> name.endsWith(".csv"));
    }

    private void processFilesMerging(File[] files, BufferedWriter writer,
        List<LibraryInfoDto> libraries) {

        AtomicBoolean headerSaved = new AtomicBoolean(false);

        Arrays.stream(files).forEach(
            file -> {
                // 파일에서 추출된 이름이 DB내의 도서관 정보에 일치한 도서관 정보를 찾는다.
                Optional<LibraryInfoDto> libraryOpt =
                    libraries.stream()
                        .filter(libray -> isContainsLibNmInFile(file, libray))
                        .findAny();

                libraryOpt.ifPresent(
                    libraryDto -> {
                        try {
                            normalizeAndWrite(file, headerSaved.get(), writer, libraryDto);
                            headerSaved.set(true);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
            });
    }

    private boolean isContainsLibNmInFile(File file, LibraryInfoDto library) {
        return library.getLibNm().contains(extractLibraryName(file));
    }

    private void normalizeAndWrite(File file, boolean headerSaved, BufferedWriter writer,
        LibraryInfoDto library) throws IOException {

        try (Reader reader = Files.newBufferedReader(
            file.toPath(), Charset.forName("EUC-KR"))) {

            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

            if (!headerSaved) {
                writer.write(HEADER_NAME);
                writer.newLine();
            }

            for (CSVRecord record : csvParser){
                normalizeData(writer, record, library);
            }
        }
    }

    // File Name : 구성도서관 장서 대출목록 (2023년 04월)에서  '구성도서관'만 추출
    private String extractLibraryName(File file) {
        return file.getName().split(" ", 3)[0];
    }

    private void normalizeData(BufferedWriter writer, CSVRecord record, LibraryInfoDto library)
        throws IOException {

        String isbn = record.get(ISBN_IDX);
        String loanCount = record.get(LOAN_CNT_IDX);
        String regisDate = record.get(REGISTER_DATE_IDX);

        if (isValidIsbn(isbn)) {
            writer.write(buildCsvLine(isbn, loanCount, library, regisDate));
            writer.newLine();
        }
    }

    private boolean isValidIsbn(String isbn) {
        Pattern pattern = Pattern.compile(ISBN_REGEX);
        Matcher matcher = pattern.matcher(isbn);

        return matcher.matches() && isbn.length() > ISBN_MIN_SIZE;
    }

    private String buildCsvLine(String isbn, String loanCount, LibraryInfoDto library,
        String regisDate) {

        return String.join(",", isbn, loanCount, library.getLibNo().toString(), regisDate,
            library.getAreaCd().toString());
    }


}