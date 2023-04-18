package com.scaling.libraryservice.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.dto.ApiBookExistDto;
import com.scaling.libraryservice.dto.LoanItemDto;
import com.scaling.libraryservice.exception.OpenApiException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiBindService {

    @Timer //Http 응답 결과를 필요한 객체로 Mapping 하고, 도서관 코드를 key로 하는 Map으로 담는다.
    public Map<Integer, ApiBookExistDto> getBookExistMap(
        List<ResponseEntity<String>> apiResponse) throws OpenApiException {

        Objects.requireNonNull(apiResponse);

        Map<Integer, ApiBookExistDto> respOpenApiDtoMap = new HashMap<>();

        for (ResponseEntity<String> r : apiResponse) {

            JSONObject respJsonObj = getJsonObjFromResponse(r);

            ApiBookExistDto apiBookExistDto = new ApiBookExistDto(
                respJsonObj.getJSONObject("request")
                , respJsonObj.getJSONObject("result"));

            respOpenApiDtoMap.put(Integer.valueOf(apiBookExistDto.getLibCode()),
                apiBookExistDto);
        }

        return respOpenApiDtoMap;
    }

    public List<LoanItemDto> getLoanItem(ResponseEntity<String> responseEntity)
        throws OpenApiException {

        JSONObject respJsonObj = getJsonObjFromResponse(responseEntity);

        List<LoanItemDto> result = new ArrayList<>();

        JSONArray jsonArray = respJsonObj.getJSONArray("docs");

        for (int i = 0; i < jsonArray.length(); i++) {

            result.add(new LoanItemDto(jsonArray.getJSONObject(i).getJSONObject("doc")));
        }

        return result;
    }

    private JSONObject getJsonObjFromResponse(ResponseEntity<String> responseEntity)
        throws OpenApiException {

        Objects.requireNonNull(responseEntity);

        JSONObject respJsonObj =
            new JSONObject(responseEntity.getBody()).getJSONObject("response");

        if (respJsonObj.has("error")) {
            String error = respJsonObj.getString("error");
            log.info("[data4library bookExist API error] message :" + error);

            throw new OpenApiException(error);
        }

        return respJsonObj;
    }

}
