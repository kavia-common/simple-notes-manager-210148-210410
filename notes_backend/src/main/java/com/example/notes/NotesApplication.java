package com.example.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PUBLIC_INTERFACE
 * NotesApplication is the Spring Boot entry point for the Notes backend service.
 * Purpose: Bootstraps the application.
 */
@SpringBootApplication
public class NotesApplication {

    // PUBLIC_INTERFACE
    public static void main(String[] args) {
        SpringApplication.run(NotesApplication.class, args);
    }
}
