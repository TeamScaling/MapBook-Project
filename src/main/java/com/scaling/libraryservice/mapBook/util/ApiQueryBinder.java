package com.scaling.libraryservice.mapBook.util;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import com.scaling.libraryservice.mapBook.dto.LoanItemDto;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiQueryBinder {

    @Timer //Http 응답 결과를 필요한 객체로 Mapping 하고, 도서관 코드를 key로 하는 Map으로 담는다.
    public Map<Integer, ApiBookExistDto> bindBookExistMap(
        List<ResponseEntity<String>> apiResponse) throws OpenApiException {

        Objects.requireNonNull(apiResponse);
        Map<Integer, ApiBookExistDto> respOpenApiDtoMap = new HashMap<>();

        for (ResponseEntity<String> response : apiResponse) {

            ApiBookExistDto apiBookExistDto = bindBookExist(response);

            respOpenApiDtoMap.put(Integer.valueOf(apiBookExistDto.getLibCode()), apiBookExistDto);
        }

        return respOpenApiDtoMap;
    }

    public ApiBookExistDto bindBookExist(ResponseEntity<String> apiResponse)
        throws OpenApiException {

        if (apiResponse == null){
            return null;
        }

        JSONObject respJsonObj = getJsonObjFromResponse(apiResponse);

        return new ApiBookExistDto(
            respJsonObj.getJSONObject("request"),
            respJsonObj.getJSONObject("result"));
    }

    public List<LoanItemDto> bindLoanItem(ResponseEntity<String> responseEntity)
        throws OpenApiException {

        JSONObject respJsonObj = getJsonObjFromResponse(responseEntity);
        JSONArray jsonArray = respJsonObj.getJSONArray("docs");

        List<LoanItemDto> result = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            result.add(new LoanItemDto(jsonArray.getJSONObject(i).getJSONObject("doc")));
        }
        return result;
    }

    private JSONObject getJsonObjFromResponse(ResponseEntity<String> responseEntity)
        throws OpenApiException {

        JSONObject respJsonObj =
            new JSONObject(responseEntity.getBody()).getJSONObject("response");

        if (respJsonObj.has("error")) {
            String error = respJsonObj.getString("error");
            log.error("[data4library bookExist API error] message :" + error);
            throw new OpenApiException(error);
        }

        return respJsonObj;
    }

}
