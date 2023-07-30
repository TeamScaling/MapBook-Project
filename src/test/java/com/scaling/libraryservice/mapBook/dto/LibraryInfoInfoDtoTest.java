package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.commons.circuitBreaker.ApiObserver;
import com.scaling.libraryservice.commons.api.apiConnection.LoanableLibConn;
import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import org.junit.jupiter.api.Test;

class LibraryInfoInfoDtoTest {

    private final Integer libNo = 141258;

    private final String isbn = "9788089365210";

    @Test
    public void load(){

        ApiConnection builder = new LoanableLibConn(libNo,isbn);

        ApiObserver api = (ApiObserver) builder;

        System.out.println(api.getApiStatus().getApiUri());

    }

}