package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.commons.api.apiConnection.BExistConn;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import org.junit.jupiter.api.Test;

class LibraryDtoTest {

    @Test
    public void load(){

        ConfigureUriBuilder builder = new BExistConn();

        ApiObserver api = (ApiObserver) builder;

        System.out.println(api.getApiStatus().getApiUri());

    }

}