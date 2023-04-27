package com.scaling.libraryservice.mapBook.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.mapBook.domain.ApiObservable;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import org.junit.jupiter.api.Test;

class LibraryDtoTest {

    @Test
    public void load(){

        ConfigureUriBuilder builder = new LibraryDto();

        ApiObservable api = (ApiObservable) builder;

        System.out.println(api.apiAccessible());

    }

}