package com.example.search.impl.exceptions;

public class NiceException extends RuntimeException {
    public NiceException() {
    }

    public NiceException(String message) {
        super(message);
    }

    public NiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

