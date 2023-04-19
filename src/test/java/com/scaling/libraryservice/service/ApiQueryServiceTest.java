package com.scaling.libraryservice.service;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.mapBook.service.ApiQueryService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.web.client.RestTemplate;

class ApiQueryServiceTest {

    private ApiQueryService apiQueryService;

    @BeforeEach
    void setUp() {

        apiQueryService = new ApiQueryService(new RestTemplate());
    }

    @Test @DisplayName("테스트를 위한 기본 세팅을 확인")
    public void check_test_setting(){
        /* given */

        /* when */

        /* then */
        System.out.println(apiQueryService);
        assertNotNull(apiQueryService);
    }

    @Test @DisplayName("도서 소장 여부 API 요청 성공")
    void singleQuery_bookExist_success() {
        /* given */

        int libNo = 141053;
        String isbn13 = "9788089365210";
        Map<String,String> paramMap = new HashMap<>();

        paramMap.put("apiUrl","http://data4library.kr/api/bookExist");
        paramMap.put("libCode", String.valueOf(libNo));
        paramMap.put("isbn13", isbn13);
        paramMap.put("format", "json");

        /* when */

        Executable executable = () -> apiQueryService.singleQuery(paramMap);

        /* then */

        assertDoesNotThrow(executable);
    }
    
    @Test @DisplayName("인기 대출 목록 API 요청 성공")
    public void loanItem_api_success(){
        /* given */

        int pageSize = 30;
        Map<String,String> paramMap = new HashMap<>();

        paramMap.put("apiUrl","http://data4library.kr/api/loanItemSrch");
        paramMap.put("pageSize", String.valueOf(pageSize));
        paramMap.put("format", "json");
        
        /* when */

        Executable executable = () -> apiQueryService.singleQuery(paramMap);

        /* then */

        assertDoesNotThrow(executable);
    }

    @Test @DisplayName("open API에 잘못된 요청을 보냈을 때 에러 처리")
    public void incorrect_libNo_error(){
        /* given */

        int libNo = 1410;
        String isbn13 = "9788089365210";
        Map<String,String> paramMap = new HashMap<>();

        paramMap.put("apiUrl","http://data4library.kr/api/bookExist");
        paramMap.put("libCode", String.valueOf(libNo));
        paramMap.put("isbn13", isbn13);
        paramMap.put("format", "json");

        /* when */


        Executable executable = () -> apiQueryService.singleQuery(paramMap);

        /* then */

        System.out.println(apiQueryService.singleQuery(paramMap));

        assertDoesNotThrow(executable);
    }

    @Test
    void multiQuery() {
    }
}