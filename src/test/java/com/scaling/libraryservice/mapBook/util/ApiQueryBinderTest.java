package com.scaling.libraryservice.mapBook.util;

import com.scaling.libraryservice.commons.api.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.api.util.ApiQueryBinder;
import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import java.io.IOException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;

@SpringBootTest
class ApiQueryBinderTest {

    private ApiQueryBinder apiQueryBinder = new ApiQueryBinder();

    @Autowired
    private ApiQuerySender apiQuerySender;

    @Test
    public void load() throws IOException {
        /* given */
        var response = apiQuerySender.sendSingleQuery(new BExistConn(), HttpEntity.EMPTY);
        /* when */
        System.out.println(response);
        /* then */

/*        var result2 = apiQueryBinder.jsonToObject(response.getBody(), ApiBookExistDto.class);*/


        JSONObject respJsonObj =
            new JSONObject(response.getBody()).getJSONObject("response");
    }
}