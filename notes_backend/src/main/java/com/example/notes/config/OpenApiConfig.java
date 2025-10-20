package com.example.notes.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PUBLIC_INTERFACE
 * OpenAPI configuration for API documentation.
 */
@Configuration
public class OpenApiConfig {

    /**
     * PUBLIC_INTERFACE
     * Creates the OpenAPI bean configuring title, description, version and tags.
     * @return OpenAPI instance.
     */
    @Bean
    public OpenAPI notesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Simple Notes Manager API")
                        .version("0.1.0")
                        .description("GxP-ready Notes CRUD API with Audit Trail and Validation")
                        .contact(new Contact().name("Notes Team").email("team@example.com")))
                .addTagsItem(new Tag().name("Notes").description("Note CRUD operations"))
                .addTagsItem(new Tag().name("Audit").description("Audit trail"))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Docs")
                        .url("https://example.com/docs"));
    }
}
