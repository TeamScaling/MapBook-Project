package com.scaling.libraryservice.commons.api.apiConnection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

class LoanableLibConnTest {

    private final Integer libNo = 141258;

    private final String isbn = "9788089365210";


    @Test @DisplayName("API 요청 내용 구성 하는 기능이 정상")
    void configUriBuilder() {
        /* given */

        String targetUri =
            "http://mockServer.kr/api/bookExist?"
                + "authKey"
                + "&isbn13="+isbn
                + "&libCode="+libNo
                + "&format=json";

        ApiConnection apiConnection = new ApiConnection() {
            @Override
            public UriComponentsBuilder configUriBuilder() {
                return UriComponentsBuilder.fromHttpUrl(targetUri);
            }
        };

        /* when */

        var result = apiConnection.configUriBuilder();

        /* then */

        assertEquals(result.toUriString(),targetUri);
    }

    @Test
    void getApiStatus() {
        /* given */

        LoanableLibConn loanableLibConn = new LoanableLibConn(libNo, isbn);

        /* when */

        var result = loanableLibConn.getApiStatus();

        /* then */

        assertNotNull(result);
    }

    @Test
    @DisplayName("다른 BExistConn 객체라도 ApiStatus는 동일하다.")
    public void equals_apiStatus() {
        /* given */

        LoanableLibConn loanableLibConn1 = new LoanableLibConn(libNo, isbn);
        LoanableLibConn loanableLibConn2 = new LoanableLibConn(libNo, isbn);

        /* when */

        var apiStatus1 = loanableLibConn1.getApiStatus();
        var apiStatus2 = loanableLibConn2.getApiStatus();

        /* then */

        assertEquals(apiStatus1, apiStatus2);
    }
}