package com.scaling.libraryservice.commons.api.apiConnection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoanItemConnTest {

    private final Integer libNo = 141258;

    private final String isbn = "9788089365210";


    @Test @DisplayName("API 요청 내용 구성 하는 기능이 정상")
    void configUriBuilder() {
        /* given */

        int pageSize = 30;

        String targetUri =
            "http://data4library.kr/api/loanItemSrch?"
                + "authKey=0f6d5c95011bddd3da9a0cc6975868d8293f79f0ed1c66e9cd84e54a43d4bb72"
                + "&pageSize="+pageSize
                + "&format=json";

        LoanItemConn loanItemConn = new LoanItemConn(pageSize);

        /* when */

        var result = loanItemConn.configUriBuilder();

        /* then */

        assertEquals(result.toUriString(),targetUri);
    }

    @Test
    void getApiStatus() {
        /* given */

        int pageSize = 30;
        LoanItemConn loanItemConn = new LoanItemConn(pageSize);

        /* when */

        var result = loanItemConn.getApiStatus();

        /* then */

        assertNotNull(result);
    }

    @Test
    @DisplayName("다른 BExistConn 객체라도 ApiStatus는 동일하다.")
    public void equals_apiStatus() {
        /* given */

        int pageSize = 30;
        LoanItemConn loanItemConn1 = new LoanItemConn(pageSize);
        LoanItemConn loanItemConn2 = new LoanItemConn(pageSize);

        /* when */

        var apiStatus1 = loanItemConn1.getApiStatus();
        var apiStatus2 = loanItemConn2.getApiStatus();

        /* then */

        assertEquals(apiStatus1, apiStatus2);
    }
}