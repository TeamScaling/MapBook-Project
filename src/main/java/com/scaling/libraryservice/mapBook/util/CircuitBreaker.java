package com.scaling.libraryservice.mapBook.util;

import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class CircuitBreaker {

    public void observeError(ConfigureUriBuilder configUriBuilder, RestClientException e){
        System.out.println(e.getMessage()+"@@@@@@@@@@@@@@@@@@@@@");

        log.error("error가 나타난 요청 api 주소 : "+configUriBuilder.getApiUrl());


        log.error("error가 접수 됐습니다. [{}]",e.toString());
    }

}
