package com.scaling.libraryservice.commons.api.service.provider;

import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import com.scaling.libraryservice.commons.api.apiConnection.KakaoBookConn;
import com.scaling.libraryservice.commons.api.apiConnection.OpenApi;
import java.util.List;

public interface DataProvider<T> {


    List<T> provideDataList(List<? extends ApiConnection> connections,int nThreads);

}
