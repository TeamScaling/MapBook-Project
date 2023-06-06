package com.scaling.libraryservice.commons.api.service.provider;

import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import com.scaling.libraryservice.commons.api.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.api.apiConnection.OpenApi;
import com.scaling.libraryservice.commons.api.service.AuthKeyLoader;
import com.scaling.libraryservice.commons.api.util.ApiQueryBinder;
import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class BExistProvider implements DataProvider<ApiBookExistDto>{

    private final ApiQuerySender apiQuerySender;
    private final ApiQueryBinder<ApiBookExistDto> apiQueryBinder;

    private final AuthKeyLoader authKeyLoader;


    @PostConstruct
    private void loadAuthKey(){
        String apiAuthKey = authKeyLoader.loadAuthKey(OpenApi.DATA4_Lib).getAuthKey();
        BExistConn.setApiAuthKey(apiAuthKey);
    }

    @Override
    public List<ApiBookExistDto> provideDataList(List<? extends ApiConnection> connections,int nThreads) {

        List<ResponseEntity<String>> responseEntities =
            apiQuerySender.sendMultiQuery(connections, nThreads, HttpEntity.EMPTY);

        return apiQueryBinder.bindList(responseEntities);
    }
}
