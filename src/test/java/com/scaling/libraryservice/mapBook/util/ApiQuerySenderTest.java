package com.scaling.libraryservice.mapBook.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scaling.libraryservice.commons.circuitBreaker.ApiStatus;
import com.scaling.libraryservice.commons.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.apiConnection.LoanItemConn;
import com.scaling.libraryservice.commons.apiConnection.MockApiConn;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class ApiQuerySenderTest {
    @Autowired
    private ApiQuerySender apiQuerySender;
    private WireMockServer mockServer;
    private final String target = "9788089365210";

    private String uri = "http://localhost:8089/api/bookExist";

    @BeforeEach
    public void setUp() {
        mockServer = new WireMockServer(8089);
    }

    @Test
    @DisplayName("Server에 정상 API 요청을 보냈을 때 정상")
    public void sendSingleQuery_ToServer_return200() {
        /* given */
        String mockResponseBody = "{ \"message\": \"Success\" }";
        mockServer.stubFor(
            WireMock.get("/api/bookExist").willReturn(WireMock.okJson(mockResponseBody)));
        mockServer.start();

        ApiStatus apiStatus = new ApiStatus(uri, 5);
        MockApiConn mockApiConn = new MockApiConn(uri, apiStatus);

        /* when */

        var result = apiQuerySender.sendSingleQuery(mockApiConn, target);

        /* then */
        assertEquals(200, result.getStatusCode().value());
    }

    @Timer
    @Test
    @DisplayName(" API 늦은 서버 응답에 따른 예외 발생")
    public void apiServer_too_long_response() {
        /* given */

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(2000);

        apiQuerySender = new ApiQuerySender(new RestTemplate(factory));

        mockServer.stubFor(WireMock.get("/api/bookExist")
            .willReturn(WireMock.aResponse().withStatus(200).withFixedDelay(200000))
        );

        ApiStatus apiStatus = new ApiStatus(uri, 5);
        MockApiConn mockApiConn = new MockApiConn(uri, apiStatus);

        /* when */
        mockServer.start();

        Executable e = () -> apiQuerySender.sendSingleQuery(mockApiConn, target);
        mockServer.stop();
        /* then */

        assertThrows(RestClientException.class,e);
    }

    @Test
    @DisplayName("도서 소장 여부 API 요청 성공")
    void singleQuery_bookExist_success() {
        //given

        int libNo = 141053;
        String isbn13 = "9788089365210";

        //when
        var result
            = apiQuerySender.sendSingleQuery(new BExistConn(), isbn13);
        Executable executable = () -> apiQuerySender.sendSingleQuery(new BExistConn(), isbn13);

        //then

        assertDoesNotThrow(executable);
    }

    @Test
    @DisplayName("인기 대출 목록 API 요청 성공")
    public void loanItem_api_success() {
        //given

        int pageSize = 30;

        //when

        Executable executable = () -> apiQuerySender.sendSingleQuery(new LoanItemConn(),
            pageSize + "");

        //then

        assertDoesNotThrow(executable);
    }

    @Test
    public void operation_sendMultiquery_success() {
        /* given */
        int libNo = 141053;
        String isbn13 = "9788089365210";
        ApiStatus mockStatus = new ApiStatus(uri,10);
        MockApiConn mockApiConn = new MockApiConn(uri,mockStatus);

        List<ConfigureUriBuilder> uriBuilders = new ArrayList<>();

        for(int i=0; i<10; i++){
            uriBuilders.add(mockApiConn);
        }

        String mockResponseBody = "{ \"message\": \"Success\" }";
        mockServer.stubFor(
            WireMock.get("/api/bookExist").willReturn(WireMock.okJson(mockResponseBody)));


        /* when */
        mockServer.start();
        var result = apiQuerySender.sendMultiQuery(uriBuilders,isbn13,10);
        mockServer.stop();
        /* then */

        assertEquals(10,result.size());
    }

}