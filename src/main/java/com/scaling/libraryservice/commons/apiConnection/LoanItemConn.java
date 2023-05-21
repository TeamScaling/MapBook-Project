package com.scaling.libraryservice.commons.apiConnection;

import com.scaling.libraryservice.commons.circuitBreaker.ApiStatus;
import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 인기 대출 도서 검색 API와 연결하는 클래스입니다.
 */
public class LoanItemConn implements ApiObserver, ConfigureUriBuilder {

    private static final String API_URL = "http://data4library.kr/api/loanItemSrch";

    private static final String DEFAULT_AUTH_KEY = "0f6d5c95011bddd3da9a0cc6975868d8293f79f0ed1c66e9cd84e54a43d4bb72";
    public static final ApiStatus apiStatus = new ApiStatus(API_URL,10);

    /**
     * 주어진 페이지 크기에 대한 인기 대출 도서 검색 API URI를 구성합니다.
     *
     * @param pageSize 반환할 페이지 크기
     * @return 구성된 URI 정보를 담고 있는 {@link UriComponentsBuilder} 객체
     */
    @Override
    public UriComponentsBuilder configUriBuilder(String pageSize) {
        UriComponentsBuilder uriBuilder
            = UriComponentsBuilder.fromHttpUrl(API_URL)
            .queryParam("authKey",DEFAULT_AUTH_KEY)
            .queryParam("pageSize", pageSize)
            .queryParam("format","json");

        return uriBuilder;
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
