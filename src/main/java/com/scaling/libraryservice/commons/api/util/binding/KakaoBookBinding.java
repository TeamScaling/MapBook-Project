package com.scaling.libraryservice.commons.api.util.binding;

import com.scaling.libraryservice.batch.bookUpdate.dto.BookApiDto;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public class KakaoBookBinding implements BindingStrategy<BookApiDto> {

    /**
     * API 응답 데이터를 BookApiDto로 변환합니다.
     *
     * @param apiResponse API 응답 데이터
     * @return 변환된 BookApiDto 객체
     * @throws OpenApiException API 응답을 처리하는 도중 에러가 발생하면 예외를 던집니다.
     */
    @Override
    public BookApiDto bind(ResponseEntity<String> apiResponse) {

        if (apiResponse == null) {
            return null;
        }

        JSONArray jsonArray = new JSONObject(apiResponse.getBody()).getJSONArray("documents");

        return jsonArray.length() != 0
            ? new BookApiDto(jsonArray.getJSONObject(0)) : new BookApiDto();
    }
}
