package com.scaling.libraryservice.commons.api.service;

import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import java.util.List;

public interface DataProvider<T> {

    List<T> provideDataList(List<? extends ApiConnection> connections,int nThreads);

}
