package com.scaling.libraryservice.batch.loanCnt.util;

import com.scaling.libraryservice.batch.loanCnt.domain.LibraryCatalog;
import java.util.List;
import java.util.Map;

public class Aggregator {

    private final Map<String, Integer> isbnLoanCntMap;

    public Aggregator(Map<String, Integer> isbnLoanCntMap) {
        this.isbnLoanCntMap = isbnLoanCntMap;
    }

    public void aggregateLoanCnt(LibraryCatalog libraryCatalog) {

        String isbn = libraryCatalog.getIsbn();
        int loanCnt = libraryCatalog.getLoanCnt();

        isbnLoanCntMap.compute(isbn, (oldIsbn, oldLoanCnt) ->
            (oldLoanCnt == null ? 0 : oldLoanCnt) + loanCnt
        );
    }

    public List<LibraryCatalog> getAggregatedLibraryCatalogs() {
        return this.isbnLoanCntMap.entrySet().stream()
            .map(entry -> new LibraryCatalog(entry.getKey(), entry.getValue()))
            .toList();
    }

}
