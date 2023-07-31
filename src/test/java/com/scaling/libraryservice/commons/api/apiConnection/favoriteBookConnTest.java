package com.scaling.libraryservice.commons.api.apiConnection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class favoriteBookConnTest {

    private final Integer libNo = 141258;

    private final String isbn = "9788089365210";


    @Test @DisplayName("API 요청 내용 구성 하는 기능이 정상")
    void configUriBuilder() {
        /* given */

        int pageSize = 30;

        String targetUri =
            "http://data4library.kr/api/loanItemSrch?"
                + "authKey"
                + "&pageSize="+pageSize
                + "&format=json";

        FavoriteBookConn favoriteBookConn = new FavoriteBookConn(pageSize);

        /* when */

        var result = favoriteBookConn.configUriBuilder();

        /* then */

        assertEquals(result.toUriString(),targetUri);
    }

    @Test
    void getApiStatus() {
        /* given */

        int pageSize = 30;
        FavoriteBookConn favoriteBookConn = new FavoriteBookConn(pageSize);

        /* when */

        var result = favoriteBookConn.getApiStatus();

        /* then */

        assertNotNull(result);
    }

    @Test
    @DisplayName("다른 BExistConn 객체라도 ApiStatus는 동일하다.")
    public void equals_apiStatus() {
        /* given */

        int pageSize = 30;
        FavoriteBookConn favoriteBookConn1 = new FavoriteBookConn(pageSize);
        FavoriteBookConn favoriteBookConn2 = new FavoriteBookConn(pageSize);

        /* when */

        var apiStatus1 = favoriteBookConn1.getApiStatus();
        var apiStatus2 = favoriteBookConn2.getApiStatus();

        /* then */

        assertEquals(apiStatus1, apiStatus2);
    }
}