package com.example.notes.notes;

import com.example.notes.notes.dto.NotePatchRequest;
import com.example.notes.notes.dto.NoteRequest;
import com.example.notes.notes.dto.NoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
// ============================================================================
// REQUIREMENT TRACEABILITY
// ============================================================================
// Requirement ID: REQ-NOTES-CRUD
// User Story: As a user, I can create/read/update/delete notes.
// Acceptance Criteria: Endpoints for CRUD, validation, audit trail, RBAC scaffolding, structured errors.
// GxP Impact: YES - Data integrity and audit trail.
// Risk Level: MEDIUM
// Validation Protocol: VP-NOTES-001
// ============================================================================
 */
@RestController
@RequestMapping(path = "/api/notes", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notes", description = "CRUD operations for notes")
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    // PUBLIC_INTERFACE
    @Operation(
            summary = "Create a note",
            description = "Creates a new note. RBAC: USER or ADMIN. Header X-User-Id required for attribution.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created",
                            content = @Content(schema = @Schema(implementation = NoteResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error",
                            content = @Content(schema = @Schema(implementation = com.example.notes.exception.ApiError.class)))
            }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse create(@Valid @RequestBody NoteRequest request) {
        // RBAC placeholder: @PreAuthorize("hasAnyRole('USER','ADMIN')")
        return NoteMapper.toResponse(service.create(request));
    }

    // PUBLIC_INTERFACE
    @Operation(
            summary = "List notes",
            description = "Returns all notes. RBAC: USER or ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = NoteResponse.class))))
            }
    )
    @GetMapping
    public List<NoteResponse> list() {
        return service.list().stream().map(NoteMapper::toResponse).collect(Collectors.toList());
    }

    // PUBLIC_INTERFACE
    @Operation(
            summary = "Get note by id",
            description = "Returns a note by id. RBAC: USER or ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = NoteResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(schema = @Schema(implementation = com.example.notes.exception.ApiError.class)))
            }
    )
    @GetMapping("/{id}")
    public NoteResponse get(@Parameter(description = "Note id", required = true) @PathVariable("id") UUID id) {
        return NoteMapper.toResponse(service.get(id));
    }

    // PUBLIC_INTERFACE
    @Operation(
            summary = "Update note (full)",
            description = "Replaces a note. RBAC: USER or ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = NoteResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error",
                            content = @Content(schema = @Schema(implementation = com.example.notes.exception.ApiError.class))),
                    @ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(schema = @Schema(implementation = com.example.notes.exception.ApiError.class)))
            }
    )
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public NoteResponse update(@PathVariable("id") UUID id, @Valid @RequestBody NoteRequest request) {
        return NoteMapper.toResponse(service.update(id, request));
    }

    // PUBLIC_INTERFACE
    @Operation(
            summary = "Patch note (partial)",
            description = "Partially updates a note. RBAC: USER or ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = NoteResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error",
                            content = @Content(schema = @Schema(implementation = com.example.notes.exception.ApiError.class))),
                    @ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(schema = @Schema(implementation = com.example.notes.exception.ApiError.class)))
            }
    )
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public NoteResponse patch(@PathVariable("id") UUID id, @Valid @RequestBody NotePatchRequest patch) {
        return NoteMapper.toResponse(service.patch(id, patch));
    }

    // PUBLIC_INTERFACE
    @Operation(
            summary = "Delete note",
            description = "Deletes a note by id. RBAC: ADMIN recommended.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(schema = @Schema(implementation = com.example.notes.exception.ApiError.class)))
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        // RBAC placeholder: @PreAuthorize("hasRole('ADMIN')")
        service.delete(id);
    }
}
