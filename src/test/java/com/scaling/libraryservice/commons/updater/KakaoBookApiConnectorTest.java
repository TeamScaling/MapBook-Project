package com.scaling.libraryservice.commons.updater;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.commons.api.connector.KakaoBookApiConnector;
import com.scaling.libraryservice.commons.api.apiConnection.KakaoBookConn;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import com.scaling.libraryservice.commons.api.util.ApiQueryBinder;
import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class KakaoBookApiConnectorTest {

    private KakaoBookApiConnector kakaoBookApiConnector;

    @BeforeEach
    public void setUp(){
        this.kakaoBookApiConnector = new KakaoBookApiConnector(new ApiQuerySender(new RestTemplate()),new ApiQueryBinder());
    }

    @Test @DisplayName("getBookMulti 메소드를 통해 BookApiDto list를 반환 받는 데 성공")
    public void test_getBookMulti(){
        /* given */
        String target = "9791170522553";
        String target2 = "9788954699051";
        String target3 = "9791198060532";
        String target4 = "9788955479904";
        String target5 = "9791169811248";

        List<ConfigureUriBuilder> conns = new ArrayList<>();

        conns.add(new KakaoBookConn(target));
        conns.add(new KakaoBookConn(target2));
        conns.add(new KakaoBookConn(target3));
        conns.add(new KakaoBookConn(target4));
        conns.add(new KakaoBookConn(target5));


        /* when */

        var result = kakaoBookApiConnector.getBookMulti(conns,5);
        /* then */

        assertEquals(5,result.size());

    }

}