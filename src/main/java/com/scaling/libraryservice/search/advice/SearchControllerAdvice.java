package com.scaling.libraryservice.search.advice;

import com.scaling.libraryservice.search.exception.NotQualifiedQueryException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class SearchControllerAdvice {

    @ExceptionHandler(NotQualifiedQueryException.class)
    public ResponseEntity<Object> handleDuplicateException(Exception e){

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
