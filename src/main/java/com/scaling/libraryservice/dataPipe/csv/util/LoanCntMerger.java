package com.scaling.libraryservice.dataPipe.csv.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

// 1억 2천만 대출 횟수 관련 데이터를 분할/합산/병합 프로세스에서 합산을 위한 클래스
@Slf4j
public class LoanCntMerger {

    private static final String HEADER_NAME = "ISBN,LOAN_CNT";
    private static final int ISBN_IDX = 0;
    private static final int LOAN_CNT_IDX = 1;
    private static final String DEFAULT_OUTPUT_FORMAT = "%s_%d.csv";

    public static void main(String[] args) {

        LoanCntMerger.mergeLoanCntFiles("mergeLoanCnt",
            "loanSumFile\\loanSum",
            DEFAULT_OUTPUT_FORMAT
        );
    }

    // 주어진 folder에서 file 마다 작업을 수행.
    public static void mergeLoanCntFiles(String inPutFolder, String outPutNm, String format) {

        log.info("[LoanCntMerger] is start");

        AtomicInteger fileCount = new AtomicInteger();

        // file마다 ISBN 별로 loan_cnt를 sum 하는 작업을 수행 합니다.
        Arrays.stream(getCsvFiles(inPutFolder))
            .forEach(file ->
                mergeCnt(file, String.format(format, outPutNm, fileCount.getAndIncrement()))
            );

        log.info("[LoanCntMerger] is completed");
    }


    private static void mergeCnt(File file, String outPutNm) {

        Map<String, Integer> isbnLoanCntMap = new HashMap<>();

        try (
            Reader reader = Files.newBufferedReader(file.toPath());

            BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(outPutNm),
                StandardCharsets.UTF_8)
        ) {
            // csv file에서 데이터를 읽어 온다.
            CSVParser csvParser = constructParser(reader);

            // 읽어온 csv 데이터를 isbn별로 loanCnt를 합산하여 map에 넣는다
            csvParser.forEach(record -> sumLoanCntPutMap(record, isbnLoanCntMap));

            // 다시 map에 담겨진 데이터를 Csv file로 write하여 outPut 한다.
            for (Map.Entry<String, Integer> isbnLoanCntEntry : isbnLoanCntMap.entrySet()) {
                writeToCsv(writer, isbnLoanCntEntry);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void writeToCsv(BufferedWriter writer,
        Map.Entry<String, Integer> entry) throws IOException {

        String dataLine = buildCsvLine(entry.getKey(), String.valueOf(entry.getValue()));

        writer.write(dataLine);
        writer.newLine();
    }

    private static void sumLoanCntPutMap(CSVRecord csvRecord, Map<String, Integer> map) {

        String newIsbn = csvRecord.get(ISBN_IDX);
        String newLoanCnt = csvRecord.get(LOAN_CNT_IDX);

        map.compute(newIsbn, (oldIsbn, oldLoanCnt) ->
            (oldLoanCnt == null ? 0 : oldLoanCnt) + Integer.parseInt(newLoanCnt)
        );
    }

    private static String buildCsvLine(String... args) {
        return String.join(",", args);
    }

    private static File[] getCsvFiles(String folder) {
        return new File(folder).listFiles();
    }

    private static CSVParser constructParser(Reader reader) throws IOException {
        return new CSVParser(
            reader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader()
        );
    }

    public static String getDefaultOutputFormat(){
        return DEFAULT_OUTPUT_FORMAT;
    }

}
