package com.scaling.libraryservice.commons.api.util;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.commons.api.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.api.util.binding.BookExistBinding;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

class ApiQueryBinderTest {

    private ApiQueryBinder<ApiBookExistDto> apiQueryBinder;

    private ApiQuerySender apiQuerySender;

    @BeforeEach
    public void setUp(){

        this.apiQueryBinder = new ApiQueryBinder<>(new BookExistBinding());
        this.apiQuerySender = new ApiQuerySender(new RestTemplate());
    }


    @Test
    public void test_bind(){
        /* given */

        var response = apiQuerySender.sendSingleQuery(new BExistConn(), HttpEntity.EMPTY);
        /* when */

        var result = apiQueryBinder.bind(response);

        /* then */

        assertNotNull(result);
        System.out.println(result);
    }

}