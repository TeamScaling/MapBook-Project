package com.scaling.libraryservice.dataPipe.libraryCatalog;

import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LibraryCatalogNormalizer {

    private static final int ISBN_IDX = 5;
    private static final int LOAN_CNT_IDX = 11;
    private static final int ISBN_MIN_SIZE = 10;
    private static final String ISBN_REGEX = "^\\d+$";

    private static final String LOAN_CNT_REGEX = "^-?\\d+$";


    public List<String> normalize(List<String> lines) {

       return lines.stream().map(this::extractTarget).toList();

    }

    private String extractTarget(String line) {

        StringJoiner joiner = new StringJoiner(",");
        String[] split = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        if (split.length > LOAN_CNT_IDX) {
            String isbnValue = split[ISBN_IDX].replace("\"", "");
            String loanCntValue = split[LOAN_CNT_IDX].replace("\"", "");

            if (isValidIsbn(isbnValue) && isValidLoanCnt(loanCntValue)) {
                joiner.add(isbnValue);
                joiner.add(loanCntValue);
            }
        }
        return joiner.toString();
    }


    private boolean isValidIsbn(String isbn) {
        Pattern pattern = Pattern.compile(ISBN_REGEX);
        Matcher matcher = pattern.matcher(isbn);
        return matcher.matches() && isbn.length() > ISBN_MIN_SIZE;
    }

    private boolean isValidLoanCnt(String loanCnt) {
        Pattern pattern = Pattern.compile(LOAN_CNT_REGEX);
        Matcher matcher = pattern.matcher(loanCnt);

        return matcher.matches();
    }

}