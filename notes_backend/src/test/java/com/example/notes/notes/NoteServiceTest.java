package com.example.notes.notes;

import com.example.notes.audit.AuditService;
import com.example.notes.exception.BusinessException;
import com.example.notes.exception.NotFoundException;
import com.example.notes.notes.dto.NotePatchRequest;
import com.example.notes.notes.dto.NoteRequest;
import com.example.notes.security.RequestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NoteService.
 */
public class NoteServiceTest {

    private NoteRepository repository;
    private AuditService auditService;
    private RequestContext requestContext;
    private NoteService service;

    @BeforeEach
    void setup() {
        repository = mock(NoteRepository.class);
        auditService = mock(AuditService.class);
        requestContext = new RequestContext();
        requestContext.setCurrentUserId("tester");
        service = new NoteService(repository, auditService, requestContext);
    }

    @Test
    void create_success() {
        NoteRequest req = new NoteRequest();
        req.setTitle("Title");
        req.setContent("Content");
        req.setTags("a,b");

        when(repository.save(ArgumentMatchers.any())).thenAnswer(inv -> {
            Note n = inv.getArgument(0);
            return n;
        });

        Note created = service.create(req);
        assertNotNull(created);
        assertEquals("Title", created.getTitle());
        verify(auditService, times(1)).record(eq("tester"), any(), eq("NOTE"), anyString(), isNull(), any(), anyString());
    }

    @Test
    void get_notFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.get(id));
    }

    @Test
    void update_validationTooLong() {
        UUID id = UUID.randomUUID();
        Note existing = new Note("a", "b", null, "tester");
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        NoteRequest req = new NoteRequest();
        req.setTitle("x".repeat(200));
        req.setContent("y".repeat(5201));
        assertThrows(BusinessException.class, () -> service.update(id, req));
    }

    @Test
    void patch_blankTitleRejected() {
        UUID id = UUID.randomUUID();
        Note existing = new Note("t", "c", null, "tester");
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        NotePatchRequest patch = new NotePatchRequest();
        patch.setTitle("");
        assertThrows(BusinessException.class, () -> service.patch(id, patch));
    }
}
