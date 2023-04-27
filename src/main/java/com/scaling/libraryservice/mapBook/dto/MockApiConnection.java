package com.scaling.libraryservice.mapBook.dto;

import org.springframework.web.util.UriComponentsBuilder;

public class MockApiConnection extends AbstractApiConnection {

    @Override
    public UriComponentsBuilder configUriBuilder(String target) {
        return UriComponentsBuilder.fromHttpUrl("http://localhost:" + 8089 + "/api/bookExist");
    }

    @Override
    public String getApiUrl() {
        return "http://localhost:" + 8089 + "/api/bookExist";
    }
}
