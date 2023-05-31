package com.scaling.libraryservice.commons.api.apiConnection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BExistConnTest {

    private final Integer libNo = 141258;

    private final String isbn = "9788089365210";


    @Test @DisplayName("API 요청 내용 구성 하는 기능이 정상")
    void configUriBuilder() {
        /* given */

        String targetUri =
            "http://data4library.kr/api/bookExist?"
                + "authKey=0f6d5c95011bddd3da9a0cc6975868d8293f79f0ed1c66e9cd84e54a43d4bb72"
                + "&isbn13="+isbn
                + "&libCode="+libNo
                + "&format=json";

        BExistConn bExistConn = new BExistConn(libNo, isbn);

        /* when */

        var result = bExistConn.configUriBuilder();

        /* then */

        assertEquals(result.toUriString(),targetUri);
    }

    @Test
    void getApiStatus() {
        /* given */

        BExistConn bExistConn = new BExistConn(libNo, isbn);

        /* when */

        var result = bExistConn.getApiStatus();

        /* then */

        assertNotNull(result);
    }

    @Test
    @DisplayName("다른 BExistConn 객체라도 ApiStatus는 동일하다.")
    public void equals_apiStatus() {
        /* given */

        BExistConn bExistConn1 = new BExistConn(libNo, isbn);
        BExistConn bExistConn2 = new BExistConn(libNo, isbn);

        /* when */

        var apiStatus1 = bExistConn1.getApiStatus();
        var apiStatus2 = bExistConn2.getApiStatus();

        /* then */

        assertEquals(apiStatus1, apiStatus2);
    }
}