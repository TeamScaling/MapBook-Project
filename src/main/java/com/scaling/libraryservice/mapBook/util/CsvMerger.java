package com.scaling.libraryservice.mapBook.util;

import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.entity.BookSet;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CsvMerger {

    private final LibraryFindService libraryFindService;


    public void merge() {

        List<LibraryDto> libraries = libraryFindService.getNearByLibraries(26200);

        String inputFolder = "C:\\teamScaling\\test";
        String outputFileName = "성남.csv";

        File folder = new File(inputFolder);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        boolean headerSaved = false;

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFileName),
            Charset.forName("UTF-8"))) {
            for (File file : files) {

                var split = file.getName().split(" ", 3);
                String str = split[0] + " " + split[1];

                System.out.println(str);

                var no = libraries.stream().filter(l -> l.getLibNm().contains(str)).findFirst()
                    .get().getLibNo();

                try (Reader reader = Files.newBufferedReader(file.toPath(),
                    Charset.forName("EUC-KR"));
                    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

                    if (!headerSaved) {
                        writer.write("ISBN,LOAN_CNT,LBRRY_CD,REGIS_DATA");
                        writer.newLine();
                        headerSaved = true;
                    }

                    for (CSVRecord record : csvParser) {
                        String isbn = record.get(5);
                        String loanCount = record.get(11);
                        String regisDate = record.get(12);

                        Pattern pattern = Pattern.compile("^\\d+$");
                        Matcher matcher = pattern.matcher(isbn);

                        if (matcher.matches() && isbn.length() > 10) {
                            writer.write(isbn + "," + loanCount + "," + no + "," + regisDate);
                            writer.newLine();
                        } else {
                            System.out.println("Problematic line: " + String.join(",", record));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void merge2() {

        // 입력 파일 경로와 패턴 입력
        String inputFolder = "C:\\teamScaling\\total";
        String inputFilePattern = "*.csv";

        // 출력 파일명 입력
        String outputFileName = "total_books.csv";

        File folder = new File(inputFolder);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        System.out.println(folder.exists());

        boolean headerSaved = false;

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFileName), StandardCharsets.UTF_8)) {
            for (File file : files) {
                List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

                // 헤더 처리
                if (!headerSaved) {
                    writer.write(lines.get(0));
                    writer.newLine();
                    headerSaved = true;
                }

                // 본문 처리
                for (int i = 1; i < lines.size(); i++) {
                    writer.write(lines.get(i));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void merge3() {

        // 입력 파일 경로와 패턴 입력
        String inputFolder = "C:\\teamScaling\\total1";
        String inputFilePattern = "*.csv";

        // 출력 파일명 입력
        String outputFileName = "bookTotal.csv";

        File folder = new File(inputFolder);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        System.out.println(folder.exists());

        boolean headerSaved = false;

        Set<BookSet> uniqueLines = new HashSet<>();

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFileName),
            StandardCharsets.UTF_8)) {

            for (File file : files) {
                List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

                try (Reader reader = Files.newBufferedReader(file.toPath(),
                    StandardCharsets.UTF_8);
                    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

                    // 헤더 처리
                    if (!headerSaved) {
                        writer.write(lines.get(0));
                        writer.newLine();
                        headerSaved = true;
                    }

                    for (CSVRecord record : csvParser) {
                        try {
                            String isbn = record.get(0);
                            String title = record.get(1);
                            String authr = record.get(2);
                            String publisher = record.get(3);
                            String image = record.get(4);
                            String content = record.get(5);

                            uniqueLines.add(new BookSet(isbn, title, authr, publisher, image,content));
                        } catch (IllegalStateException | ArrayIndexOutOfBoundsException e) {
                            System.err.println(
                                "Error processing line " + record.getRecordNumber() + ": "
                                    + e.getMessage());
                        }
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 중복이 제거된 데이터를 새로운 파일에 저장
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFileName),
            StandardCharsets.UTF_8)) {
            writer.write("ISBN_THIRTEEN_NO,TITLE_NM,AUTHR_NM,PUBLISHER_NM,IMAGE_URL,CONTENT");
            writer.newLine();
            for (BookSet uniqueLine : uniqueLines) {
                writer.write(uniqueLine.joinString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
