package com.scaling.libraryservice.dataPipe.csv.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

// 1억 2천만 대출 횟수 관련 데이터를 분할/합산/병합 프로세스를 위한 클래스
public class LoanCntMerger {

    private static final String HEADER_NAME = "ISBN,LOAN_CNT";
    private static final int ISBN_IDX = 0;
    private static final int LOAN_CNT_IDX = 1;


    public void execute(String inPutFolder,String outPutNm,String format)  throws Exception{

        AtomicInteger fileCount = new AtomicInteger();

        Arrays.stream(getCsvFiles(inPutFolder)).forEach(file -> {
            try {
                mergeCnt(file,String.format(format, outPutNm, fileCount.getAndIncrement()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    public void mergeCnt(File file, String outPutNm) throws Exception {

        Map<String, Integer> map = new HashMap<>();

        try (Reader reader = Files.newBufferedReader(file.toPath());
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(outPutNm),
                StandardCharsets.UTF_8)) {

            CSVParser csvParser = new CSVParser(reader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader());

            for (CSVRecord record : csvParser) {

                String isbn = record.get(ISBN_IDX);
                String loanCnt = record.get(LOAN_CNT_IDX);

                map.compute(isbn,
                    (s, integer) -> (integer == null ? 0 : integer) + Integer.parseInt(loanCnt));
            }

            for (Map.Entry<String, Integer> entry : map.entrySet()) {

                writer.write(buildCsvLine(entry.getKey(), String.valueOf(entry.getValue())));
                writer.newLine();
            }
        }
    }


    private String buildCsvLine(String... args) {
        return String.join(",", args);
    }

    private File[] getCsvFiles(String folder) {
        return new File(folder).listFiles();
    }

}
