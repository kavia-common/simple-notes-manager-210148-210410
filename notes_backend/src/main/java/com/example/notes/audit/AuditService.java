package com.example.notes.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PUBLIC_INTERFACE
 * Service to record audit trail events.
 * GxP: Ensures audit is contemporaneous, attributable, complete.
 */
@Service
public class AuditService {

    private final AuditLogRepository repository;
    private final ObjectMapper mapper;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
        this.mapper = new ObjectMapper();
    }

    /**
     * PUBLIC_INTERFACE
     * Records an audit entry.
     * @param userId user performing action
     * @param action action performed
     * @param entityType entity type, e.g., "NOTE"
     * @param entityId entity id as string
     * @param before before-state object, may be null
     * @param after after-state object, may be null
     * @param reason optional reason
     */
    @Transactional
    public void record(String userId, AuditAction action, String entityType, String entityId,
                       Object before, Object after, String reason) {
        String beforeJson = toJsonSafe(before);
        String afterJson = toJsonSafe(after);
        AuditLog log = new AuditLog(userId, action, entityType, entityId, beforeJson, afterJson, reason, null);
        repository.save(log);
    }

    /**
     * PUBLIC_INTERFACE
     * Records an error audit entry.
     * @param userId user id
     * @param entityType entity type
     * @param entityId entity id
     * @param errorDetails full error details/stack
     */
    @Transactional
    public void recordError(String userId, String entityType, String entityId, String errorDetails) {
        AuditLog log = new AuditLog(userId, AuditAction.ERROR, entityType, entityId, null, null, null, errorDetails);
        repository.save(log);
    }

    private String toJsonSafe(Object obj) {
        if (obj == null) return null;
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{\"serialization\":\"failed\"}";
        }
    }
}
