package com.scaling.libraryservice.util;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.exception.OpenApiException;
import com.scaling.libraryservice.service.ApiQueryService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.web.client.RestTemplate;

class ApiQueryServiceTest {

    private ApiQueryService sender;

    @BeforeEach
    public void setUp(){
        this.sender = new ApiQueryService(new RestTemplate());
    }


    @Test @DisplayName("open API에 잘못된 요청을 보냈을 때 에러 처리")
    public void sendQuery_error_case(){
        /* given */

        String isbn = "9788994492032";
        int libCode = 1410;

        Map<String,String> paramMap = new HashMap<>();

        paramMap.put("authKey", "41dff2848f961076d263639f9051792ef9bf91c46f0eef0c63abd1358adcb1b6");
        paramMap.put("libCode", String.valueOf(libCode));
        paramMap.put("isbn13", isbn);
        paramMap.put("format", "json");

        String apiUrl = "http://data4library.kr/api/bookEx";
        /* when */


        Executable executable = () -> sender.singleQuery(paramMap);

        /* then */

        assertThrows(OpenApiException.class,executable);
    }

    @Test @DisplayName("open API 서버에 문제가 있을 시")
    public void sendQuery_badReqeust(){
        /* given */

        String isbn = "9788994492032";
        int libCode = 1410;

        /* when */


        /* then */


    }
}