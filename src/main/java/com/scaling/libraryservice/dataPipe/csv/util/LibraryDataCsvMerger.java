package com.scaling.libraryservice.dataPipe.csv.util;

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
@Component  //다운 로드한 Csv 파일을 하나로 합친다. 작업용
public class LibraryDataCsvMerger {

    private final LibraryFindService libraryFindService;

    private static final int ISBN_IDX = 5;
    private static final int LOAN_CNT_IDX = 11;
    private static final int REGISTER_DATE_IDX = 12;
    private static final int ISBN_MIN_SIZE = 10;
    private static final String ISBN_REGEX = "^\\d+$";

    private static final String HEADER_NAME = "ISBN,LOAN_CNT,LBRRY_CD,REGIS_DATA,AREA_CD";

    // DB의 library 정보와 Csv file의 데이터 중 필요한 부분을 서로 합친다.
    public void mergeLibraryData(String inputFolder, String outputFileName,String charsetName, CSVFormat csvFormat) {

        // libarary 정보를 DB에서 모두 가져온다.
        List<LibraryInfoDto> libraries = libraryFindService.getAllLibraries();

        try (BufferedWriter writer = Files.newBufferedWriter(
            Paths.get(outputFileName), StandardCharsets.UTF_8)) {

            //가져온 library 정보와 csv file의 내용을 정규화 과정을 거치며 합친다.
            processFilesMerging(getCsvFiles(inputFolder), writer, libraries,charsetName,csvFormat);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static File[] fileLoad(String inputFolder, String fileExtension) {

        return new File(inputFolder)
            .listFiles((dir, name) -> name.endsWith(fileExtension));
    }


    private void processFilesMerging(File[] files, BufferedWriter writer,
        List<LibraryInfoDto> libraries,String charsetName, CSVFormat csvFormat) {

        AtomicBoolean headerSaved = new AtomicBoolean(false);

        Arrays.stream(files)
            .forEach(file -> {
            // 필요한 칼럼에 해당하는 데이터만 저장 한다.
            findLibraryInfo(file, libraries).ifPresent(
                libraryInfoDto -> writeToCsv(
                    file,
                    writer,
                    headerSaved,
                    libraryInfoDto,
                    charsetName,
                    csvFormat)
            );

            headerSaved.set(true);
        });
    }

    private File[] getCsvFiles(String folder) {
        return new File(folder).listFiles((dir, name) -> name.endsWith(".csv"));
    }

    // input 된 file의 이름과 일치하는 library 정보를 찾는다.
    private Optional<LibraryInfoDto> findLibraryInfo(File file, List<LibraryInfoDto> libraries) {
        return libraries.stream()
            .filter(library -> isContainsLibNmInFile(file, library))
            .findAny();
    }

    private void writeToCsv(File file, BufferedWriter writer, AtomicBoolean headerSaved,
        LibraryInfoDto library,String charsetName, CSVFormat csvFormat) {

        CSVParser csvParser = getCsvParser(file, charsetName, csvFormat);

        try {
            if (headerSaved.get()) {
                writer.write(HEADER_NAME);
                writer.newLine();
            }

            for (CSVRecord record : csvParser) {
                String result = normalizeData(record, library);
                if (!result.isBlank()) {
                    writer.write(result);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private CSVParser getCsvParser(File file, String charsetName, CSVFormat csvFormat) {

        try (Reader reader = Files.newBufferedReader(
            file.toPath(), Charset.forName(charsetName))) {
            return new CSVParser(reader, csvFormat);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String normalizeData(CSVRecord record, LibraryInfoDto library) {

        String isbn = record.get(ISBN_IDX);
        String loanCnt = record.get(LOAN_CNT_IDX);
        String regisDate = record.get(REGISTER_DATE_IDX);

        String libNo = String.valueOf(library.getLibNo());
        String areaCd = String.valueOf(library.getAreaCd());

        return isValidIsbn(isbn) ? buildCsvLine(isbn, loanCnt, regisDate, libNo, areaCd) : "";
    }

    private boolean isContainsLibNmInFile(File file, LibraryInfoDto library) {
        return library.getLibNm().contains(extractLibraryName(file));
    }

    // File Name : 구성도서관 장서 대출목록 (2023년 04월)에서  '구성도서관'만 추출
    private String extractLibraryName(File file) {
        return file.getName().split(" ", 3)[0];
    }


    private boolean isValidIsbn(String isbn) {
        Pattern pattern = Pattern.compile(ISBN_REGEX);
        Matcher matcher = pattern.matcher(isbn);

        return matcher.matches() && isbn.length() > ISBN_MIN_SIZE;
    }

    private String buildCsvLine(String... args) {
        return String.join(",", args);
    }


}