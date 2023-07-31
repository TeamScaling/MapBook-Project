package com.scaling.libraryservice.dataPipe.csv.util;

import com.scaling.libraryservice.dataPipe.vo.LoanCntVo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CsvDivider {
    private static final int ISBN_IDX = 0;
    private static final int LOAN_CNT_IDX = 1;
    
    // 큰 용량의 CSV 파일을 분할 작업하기 위한 나누기 메소드
    public void divide(String path, String outPutNm,long maxRecordsPerFile,String format) throws IOException {

        long recordCount = 0;
        long fileCount = 0;

        try (Reader reader = Files.newBufferedReader(new File(path).toPath())) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

            BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(String.format(format, outPutNm, fileCount)), StandardCharsets.UTF_8);

            for (CSVRecord record : csvParser) {
                String isbn = record.get(ISBN_IDX);
                String loanCnt = record.get(LOAN_CNT_IDX);
                LoanCntVo vo = new LoanCntVo(isbn, loanCnt);

                writer.write(buildCsvLine(vo.getIsbn(), vo.getLoanCnt()));
                writer.newLine();

                if (++recordCount % maxRecordsPerFile == 0) {
                    writer.close();
                    fileCount++;
                    writer = Files.newBufferedWriter(
                        Paths.get(String.format(format, outPutNm, fileCount)),
                        StandardCharsets.UTF_8);
                }
            }
            writer.close();
        }
    }


    private String buildCsvLine(String... args) {
        return String.join(",", args);
    }

}
