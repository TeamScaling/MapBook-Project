package com.scaling.libraryservice.mapBook.util;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.mapBook.dto.AbstractApiConnection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

        server.start();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);

        server.stubFor(WireMock.get("/api/bookExist?format=json")
            .willReturn(WireMock.aResponse().withStatus(200).withFixedDelay(200000))
        );


        AbstractApiConnection mockBuilder = new AbstractApiConnection() {
            @Override
            public UriComponentsBuilder configUriBuilder(String target) {
                return UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/api/bookExist");
            }

            @Override
            public String getApiUrl() {
                return "http://localhost:" + port + "/api/bookExist";
            }
        };

        ApiQuerySender apiQuerySender
            = new ApiQuerySender(new RestTemplate(factory),new CircuitBreaker());

        /* when */

        for(int i=0; i<10; i++){

            apiQuerySender.singleQueryJson(mockBuilder,target);
        }

        /* then */
//        assertThrows(RestClientException.class,result);
        server.stop();
    }

}