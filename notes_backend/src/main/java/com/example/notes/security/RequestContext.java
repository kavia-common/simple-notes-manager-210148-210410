package com.example.notes.security;

import org.springframework.stereotype.Component;

/**
 * PUBLIC_INTERFACE
 * Provides a simple request-scoped context to retrieve the current user id for audit purposes.
 * Note: This is a placeholder; in a real implementation, integrate with authentication.
 */
@Component
public class RequestContext {

    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();

    /**
     * PUBLIC_INTERFACE
     * Sets the current user id for the processing thread.
     * @param userId The user id parsed from incoming request header X-User-Id. Defaults to 'system' if null/blank.
     */
    public void setCurrentUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            currentUser.set("system");
        } else {
            currentUser.set(userId);
        }
    }

    /**
     * PUBLIC_INTERFACE
     * Gets the current user id for the processing thread.
     * @return user id or 'system' if not set.
     */
    public String getCurrentUserId() {
        String id = currentUser.get();
        return (id == null || id.isBlank()) ? "system" : id;
    }

    /**
     * Clears the current user id from the thread context.
     */
    public void clear() {
        currentUser.remove();
    }
}
