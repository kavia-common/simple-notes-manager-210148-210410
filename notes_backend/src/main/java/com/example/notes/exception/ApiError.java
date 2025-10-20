package com.example.notes.exception;

import java.time.OffsetDateTime;

/**
 * PUBLIC_INTERFACE
 * ApiError represents a standardized error response body.
 */
public class ApiError {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private String path;

    public ApiError() {}

    public ApiError(String code, String message, String path) {
        this.code = code;
        this.message = message;
        this.timestamp = OffsetDateTime.now();
        this.path = path;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public String getPath() { return path; }

    public void setCode(String code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
    public void setPath(String path) { this.path = path; }
}
