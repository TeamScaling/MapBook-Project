package com.scaling.libraryservice.batch.loanCnt.config;

import static java.util.Map.entry;
import static java.util.stream.Collectors.groupingBy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;

public class Test {

    public static void main(String[] args) throws IOException {

        List<LoanCnt> list = new ArrayList<>(List.of(
            new LoanCnt("123", 5),
            new LoanCnt("123", 5),
            new LoanCnt("124", 5),
            new LoanCnt("124", 3),
            new LoanCnt("125", 5),
            new LoanCnt("125", 5)
        ));

        Map<String, Integer> collect = list.stream()
            .parallel()
            .collect(
                groupingBy(LoanCnt::getIsbn, Collectors.summingInt(LoanCnt::getLoanCnt)));

        Map<String, String> stringMap = new HashMap<>(Map.ofEntries(
            entry("name", "joinjun"),
            entry("age", "33")
        ));

        stringMap.merge("name","hi",(s, s2) -> s+":"+s2);

        System.out.println(stringMap);


    }

    @Data
    static class LoanCnt {

        private String isbn;

        private int loanCnt;

        public LoanCnt(String isbn, int loanCnt) {
            this.isbn = isbn;
            this.loanCnt = loanCnt;
        }

    }

}
