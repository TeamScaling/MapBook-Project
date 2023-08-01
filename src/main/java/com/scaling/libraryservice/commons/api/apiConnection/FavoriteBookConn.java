package com.scaling.libraryservice.commons.api.apiConnection;

import com.scaling.libraryservice.commons.circuitBreaker.ApiStatus;
import com.scaling.libraryservice.commons.circuitBreaker.ApiObserver;
import org.springframework.web.util.UriComponentsBuilder;


public class FavoriteBookConn implements ApiObserver, ApiConnection {

    private static final String API_URL = "http://data4library.kr/api/loanItemSrch";

    private static String DEFAULT_AUTH_KEY;
    private static final ApiStatus apiStatus = new ApiStatus(API_URL,5);
    private int pageSize;

    public FavoriteBookConn() {
    }

    public FavoriteBookConn(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 주어진 페이지 크기에 대한 인기 대출 도서 검색 API URI를 구성합니다.
     *
     * @return 구성된 URI 정보를 담고 있는 {@link UriComponentsBuilder} 객체
     */
    @Override
    public UriComponentsBuilder configUriBuilder() {
        UriComponentsBuilder uriBuilder
            = UriComponentsBuilder.fromHttpUrl(API_URL)
            .queryParam("authKey", DEFAULT_AUTH_KEY)
            .queryParam("pageSize", pageSize)
            .queryParam("format", "json");

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
