package com.scaling.libraryservice.batch.loanCnt.domain;

import lombok.Data;

@Data
public class LibraryCatalog {

    private String isbn;

    private int loanCnt;

    public LibraryCatalog() {
    }

    public LibraryCatalog(String isbn, int loanCnt) {
        this.isbn = isbn;
        this.loanCnt = loanCnt;
    }
}
