package com.example.notes.notes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * PUBLIC_INTERFACE
 * JPA repository for Note.
 */
public interface NoteRepository extends JpaRepository<Note, UUID> {
}
