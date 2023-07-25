package com.scaling.libraryservice.commons.api.util.binding;

import com.scaling.libraryservice.mapBook.dto.LoanItemDto;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public class LoanItemBinding extends Data4LibraryBinding implements BindingStrategy<List<LoanItemDto>> {


    @Override
    public List<LoanItemDto> bind(ResponseEntity<String> apiResponse) {
        JSONObject respJsonObj = getJsonObjFromResponse(apiResponse);
        JSONArray jsonArray = respJsonObj.getJSONArray("docs");

        List<LoanItemDto> result = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            result.add(new LoanItemDto(jsonArray.getJSONObject(i).getJSONObject("doc")));
        }

        return result;
    }
}
