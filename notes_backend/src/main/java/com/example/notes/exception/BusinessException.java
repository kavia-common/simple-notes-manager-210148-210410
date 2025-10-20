package com.example.notes.exception;

/**
 * PUBLIC_INTERFACE
 * BusinessException indicates a business rule violation or validation error.
 */
public class BusinessException extends RuntimeException {
    private final String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() { return code; }
}
