package com.example.notes.validation;

import com.example.notes.exception.BusinessException;

/**
 * PUBLIC_INTERFACE
 * Validation utilities for business rules beyond Jakarta validation.
 */
public final class ValidationUtil {

    private ValidationUtil() {}

    /**
     * PUBLIC_INTERFACE
     * Ensures title and content combined length does not exceed a threshold for business constraints.
     * @param title note title
     * @param content note content
     */
    public static void validateCombinedLength(String title, String content) {
        int total = (title == null ? 0 : title.length()) + (content == null ? 0 : content.length());
        if (total > 5200) {
            throw new BusinessException("VALIDATION_ERROR", "Combined title+content length exceeds 5200 characters");
        }
    }
}
