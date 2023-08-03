package com.scaling.libraryservice.commons.circuitBreaker.exception;

public class SubstituteMethodException extends RuntimeException {

    public SubstituteMethodException() {
    }

    public SubstituteMethodException(String message) {
        super(message);
    }
}
