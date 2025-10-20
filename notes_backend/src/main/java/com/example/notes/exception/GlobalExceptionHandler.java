package com.example.notes.exception;

import com.example.notes.audit.AuditService;
import com.example.notes.security.RequestContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * PUBLIC_INTERFACE
 * Handles global exceptions, returning structured ApiError and logging to Audit trail for technical errors.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final AuditService auditService;
    private final RequestContext requestContext;

    public GlobalExceptionHandler(AuditService auditService, RequestContext requestContext) {
        this.auditService = auditService;
        this.requestContext = requestContext;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        ApiError error = new ApiError(ex.getCode(), ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessException ex, HttpServletRequest req) {
        ApiError error = new ApiError(ex.getCode(), ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                .orElse("Validation error");
        ApiError error = new ApiError("VALIDATION_ERROR", message, req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({UnexpectedRollbackException.class, Exception.class})
    public ResponseEntity<ApiError> handleTechnical(Exception ex, HttpServletRequest req) {
        String userId = requestContext.getCurrentUserId();
        // Log technical error to audit with generic entityType "SYSTEM"
        auditService.recordError(userId, "SYSTEM", "N/A", ex.getClass().getName() + ": " + ex.getMessage());
        ApiError error = new ApiError("INTERNAL_ERROR", "An unexpected error occurred", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
