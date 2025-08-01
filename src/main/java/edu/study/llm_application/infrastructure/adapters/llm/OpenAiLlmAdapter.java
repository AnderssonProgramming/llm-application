package edu.study.llm_application.infrastructure.adapters.llm;

import edu.study.llm_application.domain.entities.LlmRequest;
import edu.study.llm_application.domain.entities.LlmResponse;
import edu.study.llm_application.domain.ports.out.LlmProviderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * OpenAI implementation of LLM provider
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "openai.mock.enabled", havingValue = "false")
public class OpenAiLlmAdapter implements LlmProviderPort {
    
    private final WebClient webClient;
    
    @Value("${openai.api.key}")
    private String apiKey;
    
    @Value("${openai.api.url}")
    private String apiUrl;
    
    @Value("${openai.timeout.seconds:30}")
    private long timeoutSeconds;
    
    private static final String[] OPENAI_MODELS = {
        "gpt-3.5-turbo",
        "gpt-4",
        "gpt-4-turbo",
        "gpt-4o",
        "gpt-4o-mini"
    };
    
    @Override
    public LlmResponse generateResponse(LlmRequest request) throws LlmProviderException {
        log.info("OpenAI Provider: Processing request for model: {}", request.getModel());
        
        try {
            // Build request payload
            OpenAiRequest openAiRequest = OpenAiRequest.builder()
                    .model(request.getModel())
                    .messages(List.of(
                        OpenAiMessage.builder()
                            .role("user")
                            .content(request.getPrompt())
                            .build()
                    ))
                    .maxTokens(request.getMaxTokens())
                    .temperature(request.getTemperature())
                    .build();
            
            // Make API call
            OpenAiResponse openAiResponse = webClient
                    .post()
                    .uri(apiUrl + "/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(openAiRequest)
                    .retrieve()
                    .bodyToMono(OpenAiResponse.class)
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .block();
            
            if (openAiResponse == null || openAiResponse.getChoices().isEmpty()) {
                throw new LlmProviderException("Empty response from OpenAI API");
            }
            
            // Extract response content
            String content = openAiResponse.getChoices().get(0).getMessage().getContent();
            Integer tokensUsed = openAiResponse.getUsage() != null ? 
                openAiResponse.getUsage().getTotalTokens() : null;
            
            return LlmResponse.builder()
                    .requestId(request.getId())
                    .content(content)
                    .model(request.getModel())
                    .tokensUsed(tokensUsed)
                    .timestamp(LocalDateTime.now())
                    .status("SUCCESS")
                    .build();
                    
        } catch (Exception e) {
            log.error("OpenAI Provider: Error processing request", e);
            throw new LlmProviderException("Failed to get response from OpenAI: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean isHealthy() {
        try {
            // Simple health check - try to get models list
            webClient
                    .get()
                    .uri(apiUrl + "/models")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            
            log.debug("OpenAI Provider: Health check passed");
            return true;
            
        } catch (Exception e) {
            log.warn("OpenAI Provider: Health check failed: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public String[] getSupportedModels() {
        log.debug("OpenAI Provider: Returning supported models");
        return OPENAI_MODELS.clone();
    }
    
    @Override
    public String getProviderName() {
        return "OpenAI";
    }
    
    // DTOs for OpenAI API
    
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class OpenAiRequest {
        private String model;
        private List<OpenAiMessage> messages;
        @JsonProperty("max_tokens")
        private Integer maxTokens;
        private Double temperature;
    }
    
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class OpenAiMessage {
        private String role;
        private String content;
    }
    
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class OpenAiResponse {
        private String id;
        private String object;
        private Long created;
        private String model;
        private List<OpenAiChoice> choices;
        private OpenAiUsage usage;
    }
    
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class OpenAiChoice {
        private Integer index;
        private OpenAiMessage message;
        @JsonProperty("finish_reason")
        private String finishReason;
    }
    
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class OpenAiUsage {
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;
        @JsonProperty("completion_tokens")
        private Integer completionTokens;
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
