# Simple Notes Manager - Backend

Spring Boot 3.x (Java 17) REST API providing CRUD for Notes, with GxP compliance scaffolding:
- Audit trail (create/read/update/delete + errors)
- Validation and business rules
- RBAC placeholders (USER/ADMIN) via comments/method security scaffolding
- Global exception handling with structured error responses
- DTOs and mappers to isolate API schemas from entities
- OpenAPI docs
- Unit tests and integration test scaffolds

## Run
- Port: 3001
- H2 in-memory DB
- Swagger UI: /swagger-ui.html
- API Docs: /api-docs

## Required Header
- X-User-Id: attribute requests to a specific user for audit logs (defaults to `system` if omitted).

## Endpoints
- POST /api/notes
- GET /api/notes
- GET /api/notes/{id}
- PUT /api/notes/{id}
- PATCH /api/notes/{id}
- DELETE /api/notes/{id}

## DTO Validation
- title: 1..200 chars (required for POST/PUT)
- content: 0..5000 chars
- tags: comma-separated string up to 500 chars
- Combined title+content length <= 5200 (business rule)

## GxP Audit
- AuditLog captures userId, actionType (CREATE/READ/UPDATE/DELETE/ERROR), timestamp (ISO 8601), entityType ("NOTE"), entityId, beforeState, afterState, reason, errorDetails.
- Reads are logged with minimal metadata (count or afterState for single read).
- Errors logged via GlobalExceptionHandler.

## RBAC Scaffolding
- SecurityConfig provides method security scaffolding; endpoints currently open.
- Add `@PreAuthorize` annotations and a real auth provider to enforce roles USER/ADMIN.

## Tests
- Unit: NoteServiceTest (happy path, validation, not found)
- Integration: NoteControllerIT basic create+list scenario

## Notes
- For production, replace RequestContext and SecurityConfig with real authentication/authorization and persist audit logs in a durable store.
