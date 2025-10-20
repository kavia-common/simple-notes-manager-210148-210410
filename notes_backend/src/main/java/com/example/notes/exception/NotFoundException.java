package com.example.notes.exception;

/**
 * PUBLIC_INTERFACE
 * NotFoundException for 404 scenarios.
 */
public class NotFoundException extends RuntimeException {
    private final String code;

    public NotFoundException(String message) {
        super(message);
        this.code = "NOT_FOUND";
    }

    public String getCode() { return code; }
}
