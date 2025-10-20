package com.example.notes.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * PUBLIC_INTERFACE
 * Servlet filter to capture the X-User-Id header and populate RequestContext.
 * GxP: Ensures audit logs are attributable to users (ALCOA+).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestContextFilter extends OncePerRequestFilter {

    private final RequestContext requestContext;

    public RequestContextFilter(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String userId = request.getHeader("X-User-Id");
            requestContext.setCurrentUserId(userId);
            filterChain.doFilter(request, response);
        } finally {
            requestContext.clear();
        }
    }
}
