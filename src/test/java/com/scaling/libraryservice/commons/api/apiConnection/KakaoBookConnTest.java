package com.scaling.libraryservice.commons.api.apiConnection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class KakaoBookConnTest {

    @Test
    void configUriBuilder() {

        /* given */

        String isbn = "9791170522553";
        long id = 3423L;

        String targetUri = "https://dapi.kakao.com/v3/search/book?query="+isbn;

        KakaoBookConn kakaoBookConn = new KakaoBookConn(isbn,id);

        /* when */

        var result = kakaoBookConn.configUriBuilder();

        /* then */

        assertEquals(targetUri,result.toUriString());

    }
}