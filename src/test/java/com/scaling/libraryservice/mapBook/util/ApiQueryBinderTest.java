package com.scaling.libraryservice.mapBook.util;

import com.scaling.libraryservice.commons.apiConnection.BExistConn;
import java.io.IOException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiQueryBinderTest {

    private ApiQueryBinder apiQueryBinder = new ApiQueryBinder();

    @Autowired
    private ApiQuerySender apiQuerySender;

    @Test
    public void load() throws IOException {
        /* given */
        var response = apiQuerySender.singleQueryJson(new BExistConn(),"9788952211989");
        /* when */
        System.out.println(response);
        /* then */

/*        var result2 = apiQueryBinder.jsonToObject(response.getBody(), ApiBookExistDto.class);*/


        JSONObject respJsonObj =
            new JSONObject(response.getBody()).getJSONObject("response");
    }
}