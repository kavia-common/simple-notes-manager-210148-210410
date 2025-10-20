package com.example.notes.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * PUBLIC_INTERFACE
 * JPA repository for audit logs.
 */
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
}
