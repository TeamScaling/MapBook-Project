package com.scaling.libraryservice.commons.updater;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.commons.api.service.AuthKeyLoader;
import com.scaling.libraryservice.commons.api.service.provider.KakaoBookProvider;
import com.scaling.libraryservice.commons.api.apiConnection.KakaoBookConn;
import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KakaoBookProviderTest {

    @Autowired
    private KakaoBookProvider kakaoBookProvider;

    private AuthKeyLoader authKeyLoader;

//    @BeforeEach
//    public void setUp() {
//        this.kakaoBookProvider = new KakaoBookProvider(new ApiQuerySender(new RestTemplate()),
//            new ApiQueryBinder<>(new KakaoBookBinding()),authKeyLoader);
//    }

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

        var result = kakaoBookProvider.provideDataList(conns, 5);
        /* then */

        assertEquals(5, result.size());

    }

}