package com.scaling.libraryservice.commons.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import com.scaling.libraryservice.commons.api.util.binding.BookExistBinding;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ApiQueryBinderTest {
    private ApiQueryBinder<ApiBookExistDto> apiQueryBinder;
    @Mock
    private ApiQuerySender apiQuerySender;
    @Mock
    private ApiConnection apiConnection;

    @BeforeEach
    public void setUp(){

        this.apiQueryBinder = new ApiQueryBinder<>(new BookExistBinding());
    }


    @Test
    public void test_bind() {
        /* given */

        String targetIsbn = "9788965700036";

        String body = "{\n"
            + "    \"response\": {\n"
            + "        \"request\": {\n"
            + "            \"isbn13\": \"9788965700036\",\n"
            + "            \"libCode\": \"141099\"\n"
            + "        },\n"
            + "        \"result\": {\n"
            + "            \"hasBook\": \"Y\",\n"
            + "            \"loanAvailable\": \"N\"\n"
            + "        }\n"
            + "    }\n"
            + "}";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(body,HttpStatus.OK);

        /* when */

        Mockito.when(apiQuerySender.sendSingleQuery(apiConnection, HttpEntity.EMPTY))
            .thenReturn(responseEntity);

        var response = apiQuerySender.sendSingleQuery(apiConnection,HttpEntity.EMPTY);

        var result = apiQueryBinder.bind(response);

        /* then */

        assertEquals(result.getIsbn13(),targetIsbn);
    }

}