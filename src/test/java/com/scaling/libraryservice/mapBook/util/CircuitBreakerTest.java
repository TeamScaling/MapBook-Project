package com.scaling.libraryservice.mapBook.util;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.LoanItemDto;
import com.scaling.libraryservice.mapBook.dto.MockApiConnection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

class CircuitBreakerTest {
    

    @Test
    public void mapTest(){
        /* given */
        Map<String,Integer> observingApi = new ConcurrentHashMap<>();
        /* when */

        String apiUri = "test";

        Integer cnt = observingApi.computeIfAbsent(apiUri, s->1);
        observingApi.computeIfPresent(apiUri,(s, integer) -> integer+1);

        System.out.println(observingApi.get("test"));
        System.out.println(cnt);

        /* then */
    }

    @Timer
    @Test @DisplayName(" API 늦은 서버 응답에 따른 예외 발생")
    public void apiServer_too_long_response(){
        /* given */

        int port = 8089;
        WireMockServer server = new WireMockServer(port);

        String target = "9788089365210";
        String mockUrl = "http://localhost:" + 8089 + "/api/bookExist";

        server.start();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);

        server.stubFor(WireMock.get("/api/bookExist?format=json")
            .willReturn(WireMock.aResponse().withStatus(200).withFixedDelay(200000))
        );


        ApiQuerySender apiQuerySender
            = new ApiQuerySender(new RestTemplate(factory),new CircuitBreaker());



        /* when */

        for(int i=0; i<10; i++){

            apiQuerySender.singleQueryJson(new MockApiConnection(),target);
            System.out.println(new LibraryDto().getApiStatus().getErrorCnt());
            System.out.println(new LoanItemDto().getApiStatus().getErrorCnt());
        }

        /* then */
//        assertThrows(RestClientException.class,result);
        server.stop();
    }

}