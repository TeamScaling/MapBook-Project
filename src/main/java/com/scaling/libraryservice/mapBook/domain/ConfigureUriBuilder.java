package com.scaling.libraryservice.mapBook.domain;

import org.springframework.web.util.UriComponentsBuilder;

/**
 * Open Api 요청 param 값들을 구성 할 수 있는 인터페이스.
 */
public interface ConfigureUriBuilder {

    UriComponentsBuilder configUriBuilder();

}
