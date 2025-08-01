package edu.study.llm_application.application.controllers;

import edu.study.llm_application.application.dtos.LlmRequestDto;
import edu.study.llm_application.domain.ports.in.LlmUseCasePort;
import edu.study.llm_application.application.mappers.LlmMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.info.BuildProperties;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Integration tests for LLM Controller
 */
@WebMvcTest(LlmController.class)
public class LlmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LlmUseCasePort llmUseCase;

    @MockBean
    private LlmMapper llmMapper;

    @MockBean
    private BuildProperties buildProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetModels() throws Exception {
        // Given
        String[] models = {"mock-gpt-3.5-turbo", "mock-gpt-4"};
        when(llmUseCase.getAvailableModels()).thenReturn(models);

        // When & Then
        mockMvc.perform(get("/api/v1/llm/models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.models").exists())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.defaultModel").value("gpt-3.5-turbo"));
    }

    @Test
    public void testGetHealth() throws Exception {
        // Given
        when(llmUseCase.isServiceHealthy()).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/v1/llm/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("HEALTHY"))
                .andExpect(jsonPath("$.llmProviderHealthy").value(true));
    }

    @Test
    public void testGenerateText_ValidationError() throws Exception {
        // Given
        LlmRequestDto invalidRequest = LlmRequestDto.builder()
                .prompt("") // Invalid: empty prompt
                .model("gpt-3.5-turbo")
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/llm/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("VALIDATION_ERROR"));
    }
}
