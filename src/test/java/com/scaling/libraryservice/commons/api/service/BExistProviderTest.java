package com.scaling.libraryservice.commons.api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import com.scaling.libraryservice.commons.api.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.api.util.ApiQueryBinder;
import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import com.scaling.libraryservice.commons.circuitBreaker.CircuitBreaker;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BExistProviderTest {

    @InjectMocks
    private BExistProvider bExistProvider;
    @Mock
    private ApiQuerySender apiQuerySender;
    @Mock
    private ApiQueryBinder<ApiBookExistDto> apiQueryBinder;

    private WireMockServer mockServer;

    private final String mockUri1 = "http://mockServer.kr/api/bookExist";

    @BeforeEach
    public void setUp() {

        mockServer = new WireMockServer(8089);

        mockServer.stubFor(
            WireMock.get("/api/bookExist").willReturn(WireMock.ok()));
    }

    @Test
    public void provideDataList() {
        /* given */

//        ApiConnection bExistConn1 = new BExistConn(1, "1234");
//        ApiConnection bExistConn2 = new BExistConn(2, "1234");
//        ApiConnection bExistConn3 = new BExistConn(3, "1234");
//
//        List<ApiConnection> connections = List.of(bExistConn1, bExistConn2, bExistConn3);
//
//
//        List<ResponseEntity<String>> response = new ArrayList<>();
//
//        when(apiQuerySender.sendMultiQuery(connections, 3, HttpEntity.EMPTY)).thenReturn(
//            new ArrayList<>());
//        when(apiQueryBinder.bind(any())).thenReturn(new ApiBookExistDto("123","123","Y","Y"));
//
//        /* when */
//
//        var result = bExistProvider.provideDataList(connections, 3);
//
//        /* then */
//
//        System.out.println(result);
    }

}