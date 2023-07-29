package com.scaling.libraryservice.commons.api.apiConnection;

import com.scaling.libraryservice.commons.circuitBreaker.ApiStatus;
import com.scaling.libraryservice.commons.circuitBreaker.ApiObserver;
import lombok.Getter;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 도서관 책 보유 정보를 확인하는 API와 연결하는 클래스입니다.
 */
@Getter
public class LoanableLibConn implements ApiConnection, ApiObserver {

    private Integer libNo;

    private String isbn13;
    private static final String API_URL = "http://data4library.kr/api/bookExist";
    private static String API_AUTH_KEY;
    private static final ApiStatus apiStatus = new ApiStatus(API_URL, 10);

    private LoanableLibConn() {
    }

    public static void setApiAuthKey(String apiAuthKey) {
        API_AUTH_KEY = apiAuthKey;
    }

    public LoanableLibConn(Integer libNo, String isbn13) {
        this.libNo = libNo;
        this.isbn13 = isbn13;
    }

    /**
     * 주어진 대상 도서에 대한 도서관 책 보유 정보 API URI를 구성합니다.
     *
     * @return 구성된 URI 정보를 담고 있는 {@link UriComponentsBuilder} 객체
     */
    @Override
    public UriComponentsBuilder configUriBuilder() {

        return UriComponentsBuilder.fromHttpUrl(API_URL)
            .queryParam("authKey", API_AUTH_KEY)
            .queryParam("isbn13", isbn13)
            .queryParam("libCode", String.valueOf(this.libNo))
            .queryParam("format", "json");
    }

    /**
     * API의 상태를 반환합니다.
     *
     * @return API 상태 객체
     */
    @Override
    public ApiStatus getApiStatus() {
        return apiStatus;
    }

}
