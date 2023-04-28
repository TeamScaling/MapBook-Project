package com.scaling.libraryservice.mapBook.domain;

import org.springframework.web.util.UriComponentsBuilder;

public interface ConfigureUriBuilder {

    UriComponentsBuilder configUriBuilder(String target);

    String getApiUrl();

}
