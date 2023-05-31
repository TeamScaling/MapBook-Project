package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.commons.api.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import org.junit.jupiter.api.Test;

class LibraryDtoTest {

    private final Integer libNo = 141258;

    private final String isbn = "9788089365210";

    @Test
    public void load(){

        ApiConnection builder = new BExistConn(libNo,isbn);

        ApiObserver api = (ApiObserver) builder;

        System.out.println(api.getApiStatus().getApiUri());

    }

}