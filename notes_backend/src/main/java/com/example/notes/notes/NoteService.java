package com.example.notes.notes;

import com.example.notes.audit.AuditAction;
import com.example.notes.audit.AuditService;
import com.example.notes.exception.BusinessException;
import com.example.notes.exception.NotFoundException;
import com.example.notes.notes.dto.NotePatchRequest;
import com.example.notes.notes.dto.NoteRequest;
import com.example.notes.security.RequestContext;
import com.example.notes.validation.ValidationUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * PUBLIC_INTERFACE
 * Service layer handling note business logic with validation and auditing.
 */
@Service
public class NoteService {

    private final NoteRepository repository;
    private final AuditService auditService;
    private final RequestContext requestContext;

    public NoteService(NoteRepository repository, AuditService auditService, RequestContext requestContext) {
        this.repository = repository;
        this.auditService = auditService;
        this.requestContext = requestContext;
    }

    /**
     * PUBLIC_INTERFACE
     * Creates a new note after validation. Records audit CREATE.
     * @param request NoteRequest
     * @return created Note
     */
    @Transactional
    public Note create(@Valid NoteRequest request) {
        String userId = requestContext.getCurrentUserId();
        // RBAC placeholder: ensure USER role minimally allowed to create
        // validate beyond annotation
        ValidationUtil.validateCombinedLength(request.getTitle(), request.getContent());

        Note note = new Note(request.getTitle(), request.getContent(), request.getTags(), userId);
        Note saved = repository.save(note);

        auditService.record(userId, AuditAction.CREATE, "NOTE", saved.getId().toString(), null, saved, "create note");
        return saved;
    }

    /**
     * PUBLIC_INTERFACE
     * Returns all notes, logs READ event with minimal metadata.
     * @return list of notes
     */
    @Transactional(readOnly = true)
    public List<Note> list() {
        String userId = requestContext.getCurrentUserId();
        List<Note> notes = repository.findAll();
        // GxP: READ logging - minimal to avoid excessive storage; we log count
        auditService.record(userId, AuditAction.READ, "NOTE", "BULK", null, "{\"count\":" + notes.size() + "}", "list notes");
        return notes;
    }

    /**
     * PUBLIC_INTERFACE
     * Retrieves a note by id and logs READ.
     * @param id UUID
     * @return Note
     */
    @Transactional(readOnly = true)
    public Note get(UUID id) {
        String userId = requestContext.getCurrentUserId();
        Note note = repository.findById(id).orElseThrow(() -> new NotFoundException("Note not found: " + id));
        auditService.record(userId, AuditAction.READ, "NOTE", id.toString(), null, note, "get note");
        return note;
    }

    /**
     * PUBLIC_INTERFACE
     * Full update (PUT) of a note with validation. Records UPDATE with before/after.
     * @param id UUID
     * @param request NoteRequest
     * @return updated Note
     */
    @Transactional
    public Note update(UUID id, @Valid NoteRequest request) {
        String userId = requestContext.getCurrentUserId();
        ValidationUtil.validateCombinedLength(request.getTitle(), request.getContent());

        Note existing = repository.findById(id).orElseThrow(() -> new NotFoundException("Note not found: " + id));
        Note before = cloneForAudit(existing);

        NoteMapper.apply(existing, request);
        existing.setUpdatedAt(OffsetDateTime.now());
        existing.setUpdatedBy(userId);
        Note saved = repository.save(existing);

        auditService.record(userId, AuditAction.UPDATE, "NOTE", id.toString(), before, saved, "full update");
        return saved;
    }

    /**
     * PUBLIC_INTERFACE
     * Partial update (PATCH) of a note. Records UPDATE with before/after.
     * @param id UUID
     * @param patch NotePatchRequest
     * @return updated Note
     */
    @Transactional
    public Note patch(UUID id, @Valid NotePatchRequest patch) {
        String userId = requestContext.getCurrentUserId();

        Note existing = repository.findById(id).orElseThrow(() -> new NotFoundException("Note not found: " + id));
        Note before = cloneForAudit(existing);

        if (patch.getTitle() != null && patch.getTitle().isBlank()) {
            throw new BusinessException("VALIDATION_ERROR", "title must not be blank");
        }
        String newTitle = patch.getTitle() != null ? patch.getTitle() : existing.getTitle();
        String newContent = patch.getContent() != null ? patch.getContent() : existing.getContent();
        ValidationUtil.validateCombinedLength(newTitle, newContent);

        if (patch.getTitle() != null) existing.setTitle(patch.getTitle());
        if (patch.getContent() != null) existing.setContent(patch.getContent());
        if (patch.getTags() != null) existing.setTags(patch.getTags());
        existing.setUpdatedAt(OffsetDateTime.now());
        existing.setUpdatedBy(userId);

        Note saved = repository.save(existing);
        auditService.record(userId, AuditAction.UPDATE, "NOTE", id.toString(), before, saved, "partial update");
        return saved;
    }

    /**
     * PUBLIC_INTERFACE
     * Deletes a note and records DELETE with before-state.
     * @param id UUID
     */
    @Transactional
    public void delete(UUID id) {
        String userId = requestContext.getCurrentUserId();
        Note existing = repository.findById(id).orElseThrow(() -> new NotFoundException("Note not found: " + id));
        Note before = cloneForAudit(existing);
        repository.delete(existing);
        auditService.record(userId, AuditAction.DELETE, "NOTE", id.toString(), before, null, "delete note");
    }

    private Note cloneForAudit(Note n) {
        Note c = new Note(n.getTitle(), n.getContent(), n.getTags(), n.getCreatedBy());
        try {
            // manually mirror fields
            java.lang.reflect.Field idF = Note.class.getDeclaredField("id");
            idF.setAccessible(true);
            idF.set(c, n.getId());
        } catch (Exception ignored) {}
        c.setUpdatedAt(n.getUpdatedAt());
        c.setUpdatedBy(n.getUpdatedBy());
        return c;
    }
}
