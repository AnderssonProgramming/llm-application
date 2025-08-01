package edu.study.llm_application.domain.usecases;

import edu.study.llm_application.domain.entities.LlmRequest;
import edu.study.llm_application.domain.entities.LlmResponse;
import edu.study.llm_application.domain.ports.out.LlmProviderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for LLM Use Case
 */
@ExtendWith(MockitoExtension.class)
class LlmUseCaseTest {

    @Mock
    private LlmProviderPort llmProviderPort;

    private LlmUseCase llmUseCase;

    @BeforeEach
    void setUp() {
        llmUseCase = new LlmUseCase(llmProviderPort);
    }

    @Test
    void testValidateRequest_ValidRequest() {
        // Given
        LlmRequest validRequest = LlmRequest.builder()
                .prompt("Test prompt")
                .model("gpt-3.5-turbo")
                .maxTokens(100)
                .temperature(0.7)
                .build();

        // When
        boolean result = llmUseCase.validateRequest(validRequest);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void testValidateRequest_EmptyPrompt() {
        // Given
        LlmRequest invalidRequest = LlmRequest.builder()
                .prompt("")
                .model("gpt-3.5-turbo")
                .build();

        // When
        boolean result = llmUseCase.validateRequest(invalidRequest);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void testValidateRequest_NullRequest() {
        // When
        boolean result = llmUseCase.validateRequest(null);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void testValidateRequest_TooManyTokens() {
        // Given
        LlmRequest invalidRequest = LlmRequest.builder()
                .prompt("Test prompt")
                .model("gpt-3.5-turbo")
                .maxTokens(5000) // Too many tokens
                .build();

        // When
        boolean result = llmUseCase.validateRequest(invalidRequest);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void testGetAvailableModels() {
        // Given
        String[] mockModels = {"gpt-3.5-turbo", "gpt-4"};
        when(llmProviderPort.getSupportedModels()).thenReturn(mockModels);

        // When
        String[] result = llmUseCase.getAvailableModels();

        // Then
        assertThat(result).isEqualTo(mockModels);
    }

    @Test
    void testIsServiceHealthy() {
        // Given
        when(llmProviderPort.isHealthy()).thenReturn(true);

        // When
        boolean result = llmUseCase.isServiceHealthy();

        // Then
        assertThat(result).isTrue();
    }
}
