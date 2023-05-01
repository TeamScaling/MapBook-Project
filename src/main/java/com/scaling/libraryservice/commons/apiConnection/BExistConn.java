package com.scaling.libraryservice.commons.apiConnection;

import com.scaling.libraryservice.mapBook.domain.ApiObservable;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import com.scaling.libraryservice.mapBook.dto.ApiStatus;
import org.springframework.web.util.UriComponentsBuilder;

public class BExistConn implements ConfigureUriBuilder, ApiObservable {

    private Integer libNo;
    private static final String API_URL = "http://data4library.kr/api/bookExist";
    private static final String DEFAULT_AUTH_KEY = "55db267f8f05b0bf8e23e8d3f65bb67d206a6b5ce24f5e0ee4625bcf36e4e2bb";
    public static final ApiStatus apiStatus = new ApiStatus(API_URL,10);

    private final Integer DEFAULT_LIB_CHECKING_NO = 141258;

    private final String DEFAULT_ISBN_CHECKING = "9788089365210";

    public BExistConn() {
    }

    public BExistConn(Integer libNo) {
        this.libNo = libNo;
    }

    @Override
    public UriComponentsBuilder configUriBuilder(String target) {

        if(libNo == null){
            this.libNo = DEFAULT_LIB_CHECKING_NO;
        }

        if(target == null){
            target = DEFAULT_ISBN_CHECKING;
        }

        return UriComponentsBuilder.fromHttpUrl(API_URL)
            .queryParam("authKey", DEFAULT_AUTH_KEY)
            .queryParam("isbn13", target)
            .queryParam("libCode", String.valueOf(this.libNo));
    }

    @Override
    public  ApiStatus getApiStatus() {
        return apiStatus;
    }

}
