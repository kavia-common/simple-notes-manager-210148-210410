package com.example.notes.audit;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * PUBLIC_INTERFACE
 * AuditLog entity storing audit trail entries for GxP compliance.
 */
@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_audit_entity", columnList = "entityType, entityId"),
        @Index(name = "idx_audit_user", columnList = "userId"),
        @Index(name = "idx_audit_time", columnList = "timestamp")
})
public class AuditLog {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 100)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuditAction actionType;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    @Column(nullable = false, length = 100)
    private String entityType;

    @Column(nullable = false, length = 100)
    private String entityId;

    @Lob
    private String beforeState;

    @Lob
    private String afterState;

    @Column(length = 500)
    private String reason;

    @Lob
    private String errorDetails;

    public AuditLog() {}

    public AuditLog(String userId, AuditAction actionType, String entityType, String entityId,
                    String beforeState, String afterState, String reason, String errorDetails) {
        this.userId = userId;
        this.actionType = actionType;
        this.timestamp = OffsetDateTime.now();
        this.entityType = entityType;
        this.entityId = entityId;
        this.beforeState = beforeState;
        this.afterState = afterState;
        this.reason = reason;
        this.errorDetails = errorDetails;
    }

    public UUID getId() { return id; }
    public String getUserId() { return userId; }
    public AuditAction getActionType() { return actionType; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public String getEntityType() { return entityType; }
    public String getEntityId() { return entityId; }
    public String getBeforeState() { return beforeState; }
    public String getAfterState() { return afterState; }
    public String getReason() { return reason; }
    public String getErrorDetails() { return errorDetails; }

    public void setErrorDetails(String errorDetails) { this.errorDetails = errorDetails; }
}
