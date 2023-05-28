package com.scaling.libraryservice.commons.api.util.binding;

import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

@Slf4j
public abstract class Data4LibraryBinding {

    JSONObject getJsonObjFromResponse(ResponseEntity<String> responseEntity)
        throws OpenApiException {

        JSONObject respJsonObj =
            new JSONObject(responseEntity.getBody()).getJSONObject("response");

        if (respJsonObj.has("error")) {
            String error = respJsonObj.getString("error");
            log.error("[API error] message :" + error);
            throw new OpenApiException(error);
        }

        return respJsonObj;
    }

}
