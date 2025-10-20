package com.example.notes.notes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * PUBLIC_INTERFACE
 * DTO for Note creation or full update requests.
 */
public class NoteRequest {

    @Schema(description = "Title of the note", minLength = 1, maxLength = 200, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 1, max = 200)
    private String title;

    @Schema(description = "Content of the note", maxLength = 5000)
    @Size(max = 5000)
    private String content;

    @Schema(description = "Comma-separated tags", maxLength = 500, example = "work,personal")
    @Size(max = 500)
    private String tags;

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getTags() { return tags; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setTags(String tags) { this.tags = tags; }
}
