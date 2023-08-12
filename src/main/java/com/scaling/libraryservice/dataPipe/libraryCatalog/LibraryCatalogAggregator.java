package com.scaling.libraryservice.dataPipe.libraryCatalog;

import com.scaling.libraryservice.dataPipe.aop.BatchLogging;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// 1억 2천만 대출 횟수 관련 데이터를 분할/합산/병합 프로세스에서 합산을 위한 클래스
@Slf4j
@Component
@RequiredArgsConstructor
public class LibraryCatalogAggregator {

    private static final int ISBN_IDX = 0;
    private static final int LOAN_CNT_IDX = 1;
    private static final int NORMAL_LINE_SIZE = 2;

    @BatchLogging
    public List<String> aggregateLoanCnt(List<String> lines) {

        Map<String, Integer> isbnLoanCntMap = new HashMap<>();

        lines.stream()
            .filter(line -> line.split(",").length == NORMAL_LINE_SIZE)
            .forEach(line -> sumLoanCntPutMap(line, isbnLoanCntMap));

        return isbnLoanCntMap.entrySet().stream()
            .map(this::joinEntry)
            .toList();
    }

    private void sumLoanCntPutMap(String line, Map<String, Integer> map) {

        String[] split = line.split(",");

        String isbn = split[ISBN_IDX];
        String loanCnt = split[LOAN_CNT_IDX];

        map.compute(isbn, (oldIsbn, oldLoanCnt) ->
            (oldLoanCnt == null ? 0 : oldLoanCnt) + Integer.parseInt(loanCnt)
        );
    }

    public String joinEntry(Map.Entry<String, Integer> entry) {
        return String.join(",", entry.getKey(), String.valueOf(entry.getValue()));
    }

}
