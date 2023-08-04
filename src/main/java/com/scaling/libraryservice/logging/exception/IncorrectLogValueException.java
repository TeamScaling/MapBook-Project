package com.scaling.libraryservice.logging.exception;

public class IncorrectLogValueException extends RuntimeException{

    public IncorrectLogValueException() {
    }

    public IncorrectLogValueException(String message) {
        super(message);
    }
}
