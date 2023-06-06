package com.scaling.libraryservice.commons.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

class ApiQuerySenderTest {

    private ApiQuerySender apiQuerySender;
    private WireMockServer mockServer;
    private final String uri = "http://localhost:8089/api/bookExist";
    private final String mockResponseBody = "{ \"message\": \"Success\" }";


    @BeforeEach
    public void setUp() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(2000);
        factory.setConnectTimeout(2000);

        apiQuerySender = new ApiQuerySender(new RestTemplate(factory));
        mockServer = new WireMockServer(8089);

        mockServer.stubFor(
            WireMock.get("/api/bookExist").willReturn(WireMock.okJson(mockResponseBody)));

    }

    void setMockServerDelay(){

        mockServer.stubFor(WireMock.get("/api/bookExist")
            .willReturn(WireMock.aResponse().withStatus(200).withFixedDelay(3000))
        );
    }


    @Test @DisplayName("API 서버에 단일 요청 성공")
    void sendSingleQuery() {

        /* given */
        mockServer.start();
        /* when */

        var result = apiQuerySender.sendSingleQuery(() -> UriComponentsBuilder.fromHttpUrl(uri),
            HttpEntity.EMPTY);

        /* then */

        assertEquals(mockResponseBody, result.getBody());
        mockServer.stop();
    }

    @Test @DisplayName("API 서버에 병렬 요청 성공")
    void sendMultiQuery() {
        /* given */
        mockServer.start();

        List<ApiConnection> uriComponentsBuilders = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            uriComponentsBuilders.add(() -> UriComponentsBuilder.fromHttpUrl(uri));
        }

        /* when */

        var result = apiQuerySender.sendMultiQuery(uriComponentsBuilders, 10,
            HttpEntity.EMPTY);

        /* then */

        assertEquals( 100,result.size());

        mockServer.stop();
    }
    
    @Test @DisplayName("단일 요청에서 API 늦은 서버 응답에 따른 OpenApiException 발생")
    public void sendSingleQuery_Exception(){
        /* given */

        setMockServerDelay();
        mockServer.start();

        /* when */

        Executable e = () -> apiQuerySender.sendSingleQuery(() -> UriComponentsBuilder.fromHttpUrl(uri),
            HttpEntity.EMPTY);

        /* then */

        assertThrows(OpenApiException.class,e);
        mockServer.stop();
    }

    @Test @DisplayName("병렬 요청에서 API 늦은 서버 응답에 따른 OpenApiException 발생")
    public void sendMultiQuery_Exception(){
        /* given */
        setMockServerDelay();
        mockServer.start();


        int nRequest = 10;

        List<ApiConnection> uriComponentsBuilders = new ArrayList<>();

        for (int i = 0; i < nRequest; i++) {
            uriComponentsBuilders.add(() -> UriComponentsBuilder.fromHttpUrl(uri));
        }

        /* when */

        Executable e = () -> apiQuerySender.sendMultiQuery(uriComponentsBuilders, nRequest,
            HttpEntity.EMPTY);

        /* then */

        assertThrows(OpenApiException.class,e);
        mockServer.stop();
    }


//    @Test
//    @DisplayName("도서 소장 여부 API 요청 성공")
//    void singleQuery_bookExist_success() {
//        //given
//
//        int libNo = 141053;
//        String isbn13 = "9788089365210";
//
//        //when
//        var result
//            = apiQuerySender.sendSingleQuery(new BExistConn(libNo, isbn13), HttpEntity.EMPTY);
//        Executable executable = () -> apiQuerySender.sendSingleQuery(new BExistConn(libNo, isbn13),
//            HttpEntity.EMPTY);
//
//        //then
//
//        assertDoesNotThrow(executable);
//    }
//
//    @Test
//    @DisplayName("인기 대출 목록 API 요청 성공")
//    public void loanItem_api_success() {
//        //given
//
//        int pageSize = 30;
//
//        //when
//
//        Executable executable = () -> apiQuerySender.sendSingleQuery(new LoanItemConn(pageSize),
//            HttpEntity.EMPTY);
//
//        //then
//
//        assertDoesNotThrow(executable);
//    }

}