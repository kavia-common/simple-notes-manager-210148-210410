package com.example.notes.notes;

import com.example.notes.notes.dto.NoteRequest;
import com.example.notes.notes.dto.NoteResponse;

/**
 * PUBLIC_INTERFACE
 * Mapper utility for Note entities and DTOs.
 */
public final class NoteMapper {

    private NoteMapper() {}

    /**
     * PUBLIC_INTERFACE
     * Maps a Note entity to NoteResponse DTO.
     * @param n Note
     * @return NoteResponse
     */
    public static NoteResponse toResponse(Note n) {
        NoteResponse r = new NoteResponse();
        r.setId(n.getId());
        r.setTitle(n.getTitle());
        r.setContent(n.getContent());
        r.setTags(n.getTags());
        r.setCreatedAt(n.getCreatedAt());
        r.setUpdatedAt(n.getUpdatedAt());
        r.setCreatedBy(n.getCreatedBy());
        r.setUpdatedBy(n.getUpdatedBy());
        return r;
    }

    /**
     * PUBLIC_INTERFACE
     * Applies a NoteRequest onto an existing Note (for PUT/full update).
     * @param note Note to mutate
     * @param req NoteRequest with new values
     */
    public static void apply(Note note, NoteRequest req) {
        note.setTitle(req.getTitle());
        note.setContent(req.getContent());
        note.setTags(req.getTags());
    }
}
