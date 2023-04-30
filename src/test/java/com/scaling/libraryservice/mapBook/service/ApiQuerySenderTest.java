package com.scaling.libraryservice.mapBook.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scaling.libraryservice.commons.apiConnection.LoanItemConn;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.commons.apiConnection.BExistConn;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.LoanItemDto;
import com.scaling.libraryservice.commons.apiConnection.MockApiConn;
import com.scaling.libraryservice.mapBook.util.ApiQuerySender;
import com.scaling.libraryservice.commons.circuitBreaker.CircuitBreaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

class ApiQuerySenderTest {

    private ApiQuerySender apiQuerySender;

    private MockRestServiceServer mockServer;
    private LibraryDto libraryDto;

    private RestTemplate restTemplateForMock;

    private final String target = "9788089365210";


    @BeforeEach
    void setUp() {

        restTemplateForMock = new RestTemplate();
        RestTemplate restTemplate = new RestTemplate();

        mockServer = MockRestServiceServer.bindTo(restTemplateForMock).build();
        apiQuerySender = new ApiQuerySender(restTemplate,new CircuitBreaker());

        libraryDto = new LibraryDto(141053);

    }

    @Test @DisplayName("기본적인 MockServer 시작 테스트")
    public void mockServer_start(){
        /* given */

        mockServer.expect(MockRestRequestMatchers.requestTo("http://mockServer.kr/api/bookExist?format=json"))
            .andRespond(MockRestResponseCreators.withSuccess());

        MockApiConn.setApiUrl("http://mockServer.kr/api/bookExist");

        /* when */

        apiQuerySender = new ApiQuerySender(restTemplateForMock,new CircuitBreaker());

        apiQuerySender.singleQueryJson(new MockApiConn(),target);

        /* then */
        mockServer.verify();
    }

    @Timer
    @Test @DisplayName(" API 늦은 서버 응답에 따른 예외 발생")
    public void apiServer_too_long_response(){
        /* given */

        int port = 8089;
        WireMockServer server = new WireMockServer(port);

        server.start();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);

        server.stubFor(WireMock.get("/api/bookExist?format=json")
            .willReturn(WireMock.aResponse().withStatus(200).withFixedDelay(200000))
        );

        MockApiConn mockBuilder = new MockApiConn();

        apiQuerySender = new ApiQuerySender(new RestTemplate(factory),new CircuitBreaker());


        apiQuerySender.singleQueryJson(mockBuilder,target);

        /* then */
        System.out.println(mockBuilder.getApiStatus().getErrorCnt());
        server.stop();
    }


    @Test @DisplayName("테스트를 위한 기본 세팅을 확인")
    public void check_test_setting(){
        /* given */

        /* when */

        /* then */
        System.out.println(apiQuerySender);
        assertNotNull(apiQuerySender);
    }
    @Test @DisplayName("도서 소장 여부 API 요청 성공")
    void singleQuery_bookExist_success() {
         //given

        int libNo = 141053;
        String isbn13 = "9788089365210";

         //when

        var result
            = apiQuerySender.singleQueryJson(new BExistConn(),isbn13);
        Executable executable = () -> apiQuerySender.singleQueryJson(new BExistConn(),isbn13);

         //then

        assertDoesNotThrow(executable);
        System.out.println(result);
    }
    
    @Test @DisplayName("인기 대출 목록 API 요청 성공")
    public void loanItem_api_success(){
         //given

        int pageSize = 30;
        
         //when

        Executable executable = () -> apiQuerySender.singleQueryJson(new LoanItemConn(),pageSize+"");

         //then

        assertDoesNotThrow(executable);
    }

    @Test @DisplayName("open API에 잘못된 요청을 보냈을 때 에러 처리")
    public void incorrect_libNo_error(){
         //given

        int inCorrectLibNo = 1410;
        String isbn13 = "9788089365210";

        libraryDto.setLibNo(inCorrectLibNo);

         //when


        Executable executable = () -> apiQuerySender.singleQueryJson(new BExistConn(),isbn13);

         //then

        assertDoesNotThrow(executable);
    }

    @Test
    void is_Access_api() {

        System.out.println(apiQuerySender.checkConnection("http://data4library.kr/api/bookExist"));

    }
}