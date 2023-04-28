package com.scaling.libraryservice.config;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("com.scaling.libraryservice.mapBook.util")
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(){

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        factory.setConnectTimeout(1000);
        factory.setReadTimeout(2000);

        return new RestTemplate(factory);
    }

    @Bean
    public Komoran komoran(){

        return new Komoran(DEFAULT_MODEL.FULL);
    }


}
