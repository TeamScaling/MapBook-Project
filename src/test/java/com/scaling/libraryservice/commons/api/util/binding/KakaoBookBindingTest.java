package com.scaling.libraryservice.commons.api.util.binding;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class KakaoBookBindingTest {

    private ResponseEntity<String> responseEntity;

    private KakaoBookBinding kakaoBookBinding;

    @BeforeEach
    public void setUp(){
        String body = "{\n"
            + "    \"documents\": [\n"
            + "        {\n"
            + "            \"authors\": [\n"
            + "                \"Richard L. Daft\"\n"
            + "            ],\n"
            + "            \"contents\": \"\",\n"
            + "            \"datetime\": \"2000-10-01T00:00:00.000+09:00\",\n"
            + "            \"isbn\": \"0030316812 9780030316814\",\n"
            + "            \"price\": 33000,\n"
            + "            \"publisher\": \"Harcourt-Brace\",\n"
            + "            \"sale_price\": -1,\n"
            + "            \"status\": \"\",\n"
            + "            \"thumbnail\": \"\",\n"
            + "            \"title\": \"Organizational Behavior\",\n"
            + "            \"translators\": [],\n"
            + "            \"url\": \"https://search.daum.net/search?w=bookpage&bookId=2803518&q=Organizational+Behavior\"\n"
            + "        }\n"
            + "    ],\n"
            + "    \"meta\": {\n"
            + "        \"is_end\": true,\n"
            + "        \"pageable_count\": 1,\n"
            + "        \"total_count\": 1\n"
            + "    }\n"
            + "}";

        responseEntity = new ResponseEntity<>(body,HttpStatus.OK);
        kakaoBookBinding = new KakaoBookBinding();
    }

    @Test
    void bind() {
        /* given */

        /* when */

        var result = kakaoBookBinding.bind(responseEntity);

        /* then */

        assertNotNull(result);
    }

    @Test
    void bind_emptyResp() {
        /* given */

        String body = "{\n"
            + "    \"documents\": [],\n"
            + "    \"meta\": {\n"
            + "        \"is_end\": true,\n"
            + "        \"pageable_count\": 0,\n"
            + "        \"total_count\": 0\n"
            + "    }\n"
            + "}";

        ResponseEntity<String> response = new ResponseEntity<>(body,HttpStatus.OK);

        /* when */

        var result = kakaoBookBinding.bind(response);

        /* then */

        assertNull(result.getTitle());
    }
}