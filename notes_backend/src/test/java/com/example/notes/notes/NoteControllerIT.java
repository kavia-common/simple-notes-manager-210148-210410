package com.example.notes.notes;

import com.example.notes.NotesApplication;
import com.example.notes.notes.dto.NoteRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests (basic scaffold) for NoteController.
 */
@SpringBootTest(classes = NotesApplication.class)
@AutoConfigureMockMvc
public class NoteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void createAndList() throws Exception {
        NoteRequest req = new NoteRequest();
        req.setTitle("IT title");
        req.setContent("IT content");
        req.setTags("it");

        mockMvc.perform(post("/api/notes")
                        .header("X-User-Id", "it-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("IT title"));

        mockMvc.perform(get("/api/notes").header("X-User-Id", "it-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
