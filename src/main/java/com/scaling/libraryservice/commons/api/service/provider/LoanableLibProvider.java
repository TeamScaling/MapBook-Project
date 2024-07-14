package com.scaling.libraryservice.commons.api.service.provider;

import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import com.scaling.libraryservice.commons.api.apiConnection.LoanableLibConn;
import com.scaling.libraryservice.commons.api.apiConnection.OpenApi;
import com.scaling.libraryservice.commons.api.service.AuthKeyLoader;
import com.scaling.libraryservice.commons.api.util.ApiQueryBinder;
import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import com.scaling.libraryservice.mapBook.dto.ApiLoanableLibDto;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component // Data4Library와 apiObserver 통신해서 대출 가능 데이터를 받아온다.
public class LoanableLibProvider implements DataProvider<ApiLoanableLibDto>{

    private final ApiQuerySender apiQuerySender;

    private final ApiQueryBinder<ApiLoanableLibDto> apiQueryBinder;

    private final AuthKeyLoader authKeyLoader;

    @PostConstruct
    private void loadAuthKey(){
        String apiAuthKey = authKeyLoader.loadAuthKey(OpenApi.DATA4_Lib).getAuthKey();
        LoanableLibConn.setApiAuthKey(apiAuthKey);
    }

    @Override
    public List<ApiLoanableLibDto> provideDataList(List<? extends ApiConnection> connections, int nThreads) {
        List<ResponseEntity<String>> responseEntities = apiQuerySender.sendMultiQuery(
            connections,
            nThreads,
            HttpEntity.EMPTY
        );
        return apiQueryBinder.bindList(responseEntities,this.getClass());
    }
}
