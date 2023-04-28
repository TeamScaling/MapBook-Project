package com.scaling.libraryservice.mapBook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CachedMapBookManagerTest {

    @Autowired
    private CachedMapBook cachedMapBook;

    /*@Test
    public void load(){
        *//* given *//*

        int port = 8089;
        WireMockServer server = new WireMockServer(port);

        String target = "9788089365210";

        server.start();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);

        server.stubFor(WireMock.get("/api/bookExist?format=json")
            .willReturn(WireMock.aResponse().withStatus(200).withFixedDelay(200000))
        );


        ApiQuerySender apiQuerySender
            = new ApiQuerySender(new RestTemplate(factory),new CircuitBreaker());

        ReqMapBookDto mockReqMapBookDto = new ReqMapBookDto("1234","37.3");

        cachedMapBook.getMapBooks(new ReqMapBookDto());

        *//* when *//*

        for(int i=0; i<10; i++){

            apiQuerySender.singleQueryJson(new MockApiConnection(),target);
        }

        *//* when *//*

        *//* then *//*
    }*/

}