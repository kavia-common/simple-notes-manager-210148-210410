package com.example.notes.notes;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * PUBLIC_INTERFACE
 * Note JPA entity representing an individual note.
 */
@Entity
@Table(name = "notes", indexes = {
        @Index(name = "idx_notes_title", columnList = "title")
})
public class Note {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Size(min = 1, max = 200)
    @Column(nullable = false, length = 200)
    private String title;

    @Size(max = 5000)
    @Column(length = 5000)
    private String content;

    @Size(max = 500)
    @Column(length = 500)
    private String tags; // comma-separated string for simplicity

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @Column(nullable = false, length = 100)
    private String createdBy;

    @Column(nullable = false, length = 100)
    private String updatedBy;

    public Note() {}

    public Note(String title, String content, String tags, String userId) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
        this.createdBy = userId;
        this.updatedBy = userId;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getTags() { return tags; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setTags(String tags) { this.tags = tags; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}
