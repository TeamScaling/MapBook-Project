package com.scaling.libraryservice.logging.exception;

public class NotFoundLoggerException extends RuntimeException{

    public NotFoundLoggerException() {
    }

    public NotFoundLoggerException(String message) {
        super(message);
    }
}
