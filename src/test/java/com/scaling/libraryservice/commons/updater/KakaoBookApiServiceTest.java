package com.scaling.libraryservice.commons.updater;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.commons.api.util.binding.KakaoBookBinding;
import com.scaling.libraryservice.commons.updater.service.KakaoBookApiService;
import com.scaling.libraryservice.commons.api.apiConnection.KakaoBookConn;
import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import com.scaling.libraryservice.commons.api.util.ApiQueryBinder;
import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class KakaoBookApiServiceTest {

    private KakaoBookApiService kakaoBookApiService;

    @BeforeEach
    public void setUp() {
        this.kakaoBookApiService = new KakaoBookApiService(new ApiQuerySender(new RestTemplate()),
            new ApiQueryBinder<>(new KakaoBookBinding()));
    }

    @Test
    @DisplayName("getBookMulti 메소드를 통해 BookApiDto list를 반환 받는 데 성공")
    public void test_getBookMulti() {
        /* given */

        List<String> targetList = List.of("9791170522553", "9788954699051", "9791198060532",
            "9788955479904", "9791169811248");

        List<ApiConnection> conns = new ArrayList<>();

        for(int i=0; i<targetList.size(); i++){

            conns.add(new KakaoBookConn(targetList.get(i), (long) i));
        }


        /* when */

        var result = kakaoBookApiService.getBookMulti(conns, 5);
        /* then */

        assertEquals(5, result.size());

    }

}