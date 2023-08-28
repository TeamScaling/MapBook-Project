package com.scaling.libraryservice.batch.loanCnt.util;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.ToString;

class LibraryCatalogUrlCrawlerTest {

    public static void main(String[] args) {

        List<LoanCnt> list = List.of(
            new LoanCnt("123", 5),
            new LoanCnt("123", 5),
            new LoanCnt("123", 5),
            new LoanCnt("1234", 5),
            new LoanCnt("1234", 5),
            new LoanCnt("1235", 5)
        );

        Map<String, Integer> collect = list.stream().parallel()
            .collect(Collectors.groupingBy(
                LoanCnt::getIsbn,
                Collectors.summingInt(value -> value.loanCnt)
            ));

        System.out.println(collect);

    }


    @ToString
    @Getter
    static class LoanCnt {

        private String isbn;

        private int loanCnt;

        public LoanCnt(String isbn, int loanCnt) {
            this.isbn = isbn;
            this.loanCnt = loanCnt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            LoanCnt loanCnt = (LoanCnt) o;
            return Objects.equals(isbn, loanCnt.isbn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(isbn);
        }

        public void sumLoanCnt(int count) {
            this.loanCnt += count;
        }
    }

}