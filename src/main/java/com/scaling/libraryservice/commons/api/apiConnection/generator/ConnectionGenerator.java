package com.scaling.libraryservice.commons.api.apiConnection.generator;

import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import java.util.List;

public interface ConnectionGenerator<T extends ApiConnection,V,E> {

    List<T> generateNecessaryConns(List<V> item,E dto);

}
