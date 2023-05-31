package com.scaling.libraryservice.commons.api.util.binding;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class Data4LibraryBindingTest {


    @Test
    void getJsonObjFromResponse_errorResp() {
        /* given */
        Data4LibraryBinding binding = new BookExistBinding();

        String body = "{\n"
            + "    \"response\": {\n"
            + "        \"error\": \"인증정보가 일치하지 않습니다.\"\n"
            + "    }\n"
            + "}";

        ResponseEntity<String> response = new ResponseEntity<>(body, HttpStatus.OK);

        /* when */

        Executable e = () -> binding.getJsonObjFromResponse(response);

        /* then */
        assertThrows(OpenApiException.class,e);
    }

}