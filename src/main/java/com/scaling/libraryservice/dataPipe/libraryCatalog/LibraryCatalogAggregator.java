package com.scaling.libraryservice.dataPipe.libraryCatalog;

import com.scaling.libraryservice.dataPipe.aop.BatchLogging;
import com.scaling.libraryservice.dataPipe.csv.util.CsvUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

// 1억 2천만 대출 횟수 관련 데이터를 분할/합산/병합 프로세스에서 합산을 위한 클래스
@Slf4j
public class LibraryCatalogAggregator {
    private static final int ISBN_IDX = 0;
    private static final int LOAN_CNT_IDX = 1;
    private static final int NORMAL_LINE_SIZE = 2;

    // 주어진 folder에서 file 마다 작업을 수행.
    @BatchLogging
    public static Path aggregateLoanCnt(String inPutFolder, String outPutNm,int group) {
        AtomicInteger fileCount = new AtomicInteger();
        List<File> fileList = Arrays.asList(getCsvFiles(inPutFolder));

        // 리스트를 그룹화
        IntStream.iterate(0, i -> i + group)
            .limit((fileList.size() + group - 1) / group)  // 그룹의 수 계산
            .mapToObj(i -> fileList.subList(i, Math.min(i + group, fileList.size())))
            .forEach(subList ->
                mergeFiles(subList, String.format("%s_%d.csv", outPutNm, fileCount.getAndIncrement()))
            );

        return Path.of(outPutNm);
    }

    private static void mergeFiles(List<File> files, String outPutNm) {

        Map<String, Integer> isbnLoanCntMap = collectIsbnLoanCounts(files);

        try (BufferedWriter writer = CsvUtils.getBufferedWriter(outPutNm)) {
            isbnLoanCntMap.entrySet()
                .forEach(entry -> {
                    try {
                        writeToCsv(writer,entry);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Map<String, Integer> collectIsbnLoanCounts(List<File> files){
        Map<String, Integer> isbnLoanCntMap = new HashMap<>();

        for (File file : files) {
            try {
                Files.readAllLines(file.toPath(), Charset.forName("EUC-KR")).stream()
                    .filter(line -> line.split(",").length == NORMAL_LINE_SIZE)
                    .forEach(line -> sumLoanCntPutMap(line,isbnLoanCntMap));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return isbnLoanCntMap;
    }


    private static void writeToCsv(BufferedWriter writer,
        Map.Entry<String, Integer> entry) throws IOException {

        String dataLine = String.join(",",entry.getKey(), String.valueOf(entry.getValue()));

        writer.write(dataLine);
        writer.newLine();
    }

    private static void sumLoanCntPutMap(String line, Map<String, Integer> map) {

        String[] split = line.split(",");

        String isbn = split[ISBN_IDX];
        String loanCnt = split[LOAN_CNT_IDX];

        map.compute(isbn, (oldIsbn, oldLoanCnt) ->
            (oldLoanCnt == null ? 0 : oldLoanCnt) + Integer.parseInt(loanCnt)
        );
    }


    private static File[] getCsvFiles(String folder) {
        return new File(folder).listFiles();
    }


}
