package com.scaling.libraryservice.commons.api.service;

import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import com.scaling.libraryservice.commons.api.util.ApiQueryBinder;
import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class BExistProvider implements DataProvider<ApiBookExistDto>{

    private final ApiQuerySender apiQuerySender;
    private final ApiQueryBinder<ApiBookExistDto> apiQueryBinder;

    @Override
    public ApiBookExistDto provide(ApiConnection apiConnection) {

        ResponseEntity<String> resp
            = apiQuerySender.sendSingleQuery(apiConnection,HttpEntity.EMPTY);

        return apiQueryBinder.bind(resp);
    }

    @Override
    public List<ApiBookExistDto> provideDataList(List<? extends ApiConnection> connections) {
        List<ResponseEntity<String>> responseEntities =
            apiQuerySender.sendMultiQuery(connections, 10, HttpEntity.EMPTY);

        return responseEntities.stream().map(apiQueryBinder::bind).toList();
    }
}
