package com.scaling.libraryservice.commons.api.util;

import com.scaling.libraryservice.commons.api.service.provider.LoanableLibProvider;
import com.scaling.libraryservice.commons.api.service.provider.KakaoBookProvider;
import com.scaling.libraryservice.commons.api.util.binding.BindingStrategy;
import com.scaling.libraryservice.commons.api.util.binding.LoanableLibBinding;
import com.scaling.libraryservice.commons.api.util.binding.KakaoBookBinding;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j @Component
public class ApiQueryBinder<T> {

    private final Map<Class<?>,BindingStrategy<?>> bindingStrategyMap;

    public ApiQueryBinder() {
        this.bindingStrategyMap = new HashMap<>();

        bindingStrategyMap.put(KakaoBookProvider.class,new KakaoBookBinding());
        bindingStrategyMap.put(LoanableLibProvider.class,new LoanableLibBinding());
    }

    @SuppressWarnings("unchecked")
    public T bind(ResponseEntity<String> apiResponse,Class<?> provider) throws OpenApiException {
        return (T) bindingStrategyMap.get(provider).bind(apiResponse);
    }

    public List<T> bindList(@NonNull List<ResponseEntity<String>> apiResponses, Class<?> provider) throws OpenApiException {

        return apiResponses.stream().map(r -> bind(r,provider)).toList();
    }

}
