package com.scaling.libraryservice.commons.api.util.binding;

import com.scaling.libraryservice.mapBook.dto.ApiLoanableLibDto;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public class BookExistBinding extends Data4LibraryBinding implements
    BindingStrategy<ApiLoanableLibDto> {

    /**
     * API 응답 데이터를 ApiBookExistDto로 변환합니다.
     *
     * @param apiResponse API 응답 데이터
     * @return 변환된 ApiBookExistDto 객체
     * @throws OpenApiException API 응답을 처리하는 도중 에러가 발생하면 예외를 던집니다.
     */
    @Override
    public ApiLoanableLibDto bind(ResponseEntity<String> apiResponse) throws OpenApiException {
        if (apiResponse == null) {
            return null;
        }

        JSONObject respJsonObj = getJsonObjFromResponse(apiResponse);

        return new ApiLoanableLibDto(
            respJsonObj.getJSONObject("request"),
            respJsonObj.getJSONObject("result"));
    }


}
