package com.scaling.libraryservice.commons.api.util;

import com.scaling.libraryservice.commons.api.util.binding.BindingStrategy;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
@RequiredArgsConstructor
public class ApiQueryBinder<T> {

    private final BindingStrategy<T> bindingStrategy;

    public T bind(ResponseEntity<String> apiResponse) throws OpenApiException {
        return bindingStrategy.bind(apiResponse);
    }

    public List<T> bindList(List<ResponseEntity<String>> apiResponses) throws OpenApiException {
        return apiResponses.stream().map(this::bind).toList();
    }

}
