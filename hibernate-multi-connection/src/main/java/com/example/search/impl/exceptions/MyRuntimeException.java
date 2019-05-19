package com.example.search.impl.exceptions;

public class MyRuntimeException extends RuntimeException {
    public MyRuntimeException() {
    }

    public MyRuntimeException(String message) {
        super(message);
    }

    public MyRuntimeException(String message, Exception e) {
        super(message, e);
    }
}

