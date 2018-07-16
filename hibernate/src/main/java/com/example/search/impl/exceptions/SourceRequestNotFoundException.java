package com.example.search.impl.exceptions;

public class SourceRequestNotFoundException extends NotFoundException {

    public SourceRequestNotFoundException(int callerId) {
        super(getMessage(callerId));
    }

    private static String getMessage(int callerId) {
        return "No Task with source request Id '" + callerId + "' exists.";
    }
}
