package com.example.notes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;

/**
 * PUBLIC_INTERFACE
 * Security configuration with placeholders for RBAC.
 * Note: No actual authentication provider is configured; endpoints are open but method-level checks can be added.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * PUBLIC_INTERFACE
     * Configures a very permissive HTTP security to keep endpoints accessible while enabling method security hooks.
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * GxP: Provides scaffolding for least-privilege enforcement.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/actuator/**", "/docs", "/", "/health", "/api/info").permitAll()
                .anyRequest().permitAll()
        );
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /**
     * PUBLIC_INTERFACE
     * Placeholder utility for role checks; actual roles resolved from security context in real implementation.
     * @param roles roles to check
     * @return AuthorizationDecision
     */
    public AuthorizationDecision hasAnyRole(Set<String> roles) {
        // Placeholder: always allow for now. Replace with proper role extraction from authentication.
        return new AuthorizationDecision(true);
    }

    /**
     * PUBLIC_INTERFACE
     * Helper to map simple role names to authorities.
     * @param role role name like "USER" or "ADMIN"
     * @return SimpleGrantedAuthority
     */
    public SimpleGrantedAuthority role(String role) {
        return new SimpleGrantedAuthority("ROLE_" + role);
    }
}
