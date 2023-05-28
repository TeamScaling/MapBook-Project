package com.scaling.libraryservice.commons.circuitBreaker;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scaling.libraryservice.commons.api.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.api.apiConnection.MockApiConn;
import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest
class CircuitBreakerTest {

    private ApiObserver apiObserver1;
    private ApiObserver apiObserver2;

    @Autowired
    private CircuitBreaker circuitBreaker;

    @Autowired
    private ApiQuerySender apiQuerySender;

    private WireMockServer wireMockServer;

    private String uri = "http://mockServer.kr/api/bookExist";

    @BeforeEach
    public void setUp() {

        this.apiObserver1 = new MockApiConn(uri,new ApiStatus(uri,10));
        this.apiObserver2 = new BExistConn();
    }

    WireMockServer generateMockServer(int timeOut, int status, int delayTime){

        int port = 8089;
        WireMockServer mockServer = new WireMockServer(port);

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(timeOut);

        mockServer.stubFor(WireMock.get("/api/bookExist")
            .willReturn(WireMock.aResponse().withStatus(status).withFixedDelay(delayTime))
        );

        return mockServer;
    }
    
    @Test @DisplayName("테스트 코드에서 구성한 WireMock Server가 정상적으로 작동하는 지 확인.")
    public void mockServer_check() throws URISyntaxException {
        /* given */
        String uri = "http://localhost:8089/api/bookExist";

        WireMockServer mockServer = generateMockServer(200,200,0);
        mockServer.start();

        URI uri1 = new URI(uri);

        /* when */

        System.out.println(uri1);

        var reuslt = apiQuerySender.sendSingleQuery(() -> UriComponentsBuilder.fromUri(uri1),
            HttpEntity.EMPTY);

        System.out.println(reuslt);
        /* then */
    }



    @Test
    @DisplayName("서킷 브레이커 error monitoring")
    public void receiveError() {
        /* given */

        /* when */

        circuitBreaker.receiveError(apiObserver1);

        /* then */

        assertEquals(1, apiObserver1.getApiStatus().getErrorCnt());
    }

    @Test
    @DisplayName("A-API 장애 모니터링이 B-API 상태에 영향을 주지 않는다. ")
    public void receiveError_independent() {
        /* given */

        /* when */

        circuitBreaker.receiveError(apiObserver1);

        /* then */

        assertEquals(0, apiObserver2.getApiStatus().getErrorCnt());
    }

    @Test
    @DisplayName("error monitor에 동시성 이슈가 일어나 error cnt 변화에 문제가 생기지 않는다")
    public void receiveError_concurrency() throws InterruptedException {
        /* given */
        ExecutorService executor = Executors.newFixedThreadPool(5);

        /* when */
        Runnable runnable = () -> {
            circuitBreaker.receiveError(apiObserver1);
        };
        Runnable runnable2 = () -> {
            circuitBreaker.receiveError(apiObserver2);
        };

        for (int i = 0; i < 100; i++) {

            executor.execute(runnable);

            if (i == 4) {
                executor.execute(runnable2);
            }
        }

        /* then */

        if (executor.awaitTermination(1, TimeUnit.SECONDS)) {
            assertEquals(1, apiObserver2.getApiStatus().getErrorCnt());
        }
    }


    @Test @DisplayName(" Mock API와 성공적으로 연결하여 API 연결 상태가 정상임으로 True를 반환")
    public void checkIsAvailable_MockApi_ReturnTrue(){
        /* given */
        String mockUri = "/api/bookExist";
        WireMockServer mockServer = generateMockServer(200,200,0);
        mockServer.start();

        String apiUri = "http://localhost:" + 8089 + mockUri;

        ApiObserver apiObserver = new MockApiConn(apiUri,new ApiStatus(apiUri,5));


        /* when */

        boolean available
            = true;

        /* then */
        assertTrue(available);
    }

    @Test @DisplayName(" API Timeout 에러에 대한 Circuit Breaker 에러 처리")
    public void receiveError_mockServer_success(){

        /* given */

        String mockUri = "/api/bookExist";
        WireMockServer mockServer = generateMockServer(200,200,200000);
        mockServer.start();

        ApiStatus apiStatus = new ApiStatus(uri,10);
        MockApiConn mockApiConn = new MockApiConn(uri,apiStatus);

        /* when */

        Executable e = () -> apiQuerySender.sendSingleQuery(mockApiConn,HttpEntity.EMPTY);

        /* then */

        assertDoesNotThrow(e);

    }

    @Test
    void closeObserver() {
    }
}