package com.scaling.libraryservice.commons.apiConnection;

import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
public class MockApiConn implements ConfigureUriBuilder, ApiObserver {

    private final String apiUrl;

    private final ApiStatus apiStatus;


    @Override
    public UriComponentsBuilder configUriBuilder(String target) {
        return UriComponentsBuilder.fromHttpUrl(apiUrl);
    }


    @Override
    public ApiStatus getApiStatus() {
        return apiStatus;
    }
}
