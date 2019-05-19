package com.example.search.impl.exceptions;



public class MutiSearchException extends MyRuntimeException {
    public MutiSearchException() {
        super();
    }

    public MutiSearchException(String message) {
        super(message);
    }

    public MutiSearchException(String message, Exception e) {
        super(message, e);
    }
}
