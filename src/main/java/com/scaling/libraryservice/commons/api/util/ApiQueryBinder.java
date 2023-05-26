package com.scaling.libraryservice.commons.api.util;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.commons.updater.dto.BookApiDto;
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

    /**
     * API 응답 데이터를 Map 객체로 변환합니다. Map의 키는 도서관 코드이며, 값은 해당 도서관의 도서 존재 유무를 나타내는 DTO입니다.
     *
     * @param apiResponse API 응답 데이터 리스트
     * @return 도서관 코드를 키로 하는 도서 존재 유무 정보 Map
     * @throws OpenApiException API 응답을 처리하는 도중 에러가 발생하면 예외를 던집니다.
     */
    @Timer
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

    /**
     * API 응답 데이터를 ApiBookExistDto로 변환합니다.
     *
     * @param apiResponse API 응답 데이터
     * @return 변환된 ApiBookExistDto 객체
     * @throws OpenApiException API 응답을 처리하는 도중 에러가 발생하면 예외를 던집니다.
     */
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

    /**
     * API 응답 데이터를 BookApiDto로 변환합니다.
     *
     * @param apiResponse API 응답 데이터
     * @return 변환된 BookApiDto 객체
     * @throws OpenApiException API 응답을 처리하는 도중 에러가 발생하면 예외를 던집니다.
     */
    public BookApiDto bindBookApi(ResponseEntity<String> apiResponse)
        throws OpenApiException {

        if (apiResponse == null){
            return null;
        }

        JSONArray jsonArray =
            new JSONObject(apiResponse.getBody()).getJSONArray("documents");

        if(jsonArray.length() != 0){
            return new BookApiDto(jsonArray.getJSONObject(0));
        }else{
            return new BookApiDto();
        }
    }

    /**
     * API 응답 데이터를 LoanItemDto 리스트로 변환합니다.
     *
     * @param responseEntity API 응답 데이터
     * @return 변환된 LoanItemDto 리스트
     * @throws OpenApiException API 응답을 처리하는 도중 에러가 발생하면 예외를 던집니다.
     */
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

    /**
     * API 응답 데이터를 JSONObject로 변환합니다.
     *
     * @param responseEntity API 응답 데이터
     * @return 변환된 JSONObject
     * @throws OpenApiException API 응답을 처리하는 도중 에러가 발생하면 예외를 던집니다.
     */
    private JSONObject getJsonObjFromResponse(ResponseEntity<String> responseEntity)
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
