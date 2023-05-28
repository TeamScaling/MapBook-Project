package com.scaling.libraryservice.commons.api.util.binding;

import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface BindingStrategy<T> {

    T bind (ResponseEntity<String> apiResponse) throws OpenApiException;
}
