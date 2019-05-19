package com.example.search.impl.exceptions;



public class NotFoundException extends MyRuntimeException {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}
