package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.domain.ApiObservable;
import com.scaling.libraryservice.apiConnection.BExistConnection;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import org.junit.jupiter.api.Test;

class LibraryDtoTest {

    @Test
    public void load(){

        ConfigureUriBuilder builder = new BExistConnection();

        ApiObservable api = (ApiObservable) builder;

        System.out.println(api.getApiStatus().getApiUri());

    }

}