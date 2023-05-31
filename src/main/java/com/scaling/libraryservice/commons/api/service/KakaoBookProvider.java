package com.scaling.libraryservice.commons.api.service;

import com.scaling.libraryservice.commons.api.apiConnection.AuthKeyLoader;
import com.scaling.libraryservice.commons.api.apiConnection.KakaoBookConn;
import com.scaling.libraryservice.commons.api.apiConnection.OpenApi;
import com.scaling.libraryservice.commons.api.util.ApiQueryBinder;
import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import com.scaling.libraryservice.commons.updater.dto.BookApiDto;
import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;


@RequiredArgsConstructor
public class KakaoBookProvider implements DataProvider<BookApiDto> {

    private final ApiQuerySender apiQuerySender;
    private final ApiQueryBinder<BookApiDto> apiQueryBinder;

    private final AuthKeyLoader authKeyLoader;
    @PostConstruct
    public void loadAuthKey(){
        String apiAuthKey = authKeyLoader.loadAuthKey(OpenApi.KAKAO_BOOK).getAuthKey();
        KakaoBookConn.setApiAuthKey(apiAuthKey);
    }

    @Override
    public List<BookApiDto> provideDataList(List<? extends ApiConnection> connections,
        int nThreads) {

        List<ResponseEntity<String>> responseEntities = apiQuerySender.sendMultiQuery(connections,
            nThreads, new KakaoBookConn("", 1L).getHttpEntity());

        return apiQueryBinder.bindList(responseEntities);
    }

}
