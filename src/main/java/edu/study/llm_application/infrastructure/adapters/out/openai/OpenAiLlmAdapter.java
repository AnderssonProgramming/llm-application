package edu.study.llm_application.infrastructure.adapters.out.openai;

import edu.study.llm_application.domain.entities.LlmRequest;
import edu.study.llm_application.domain.entities.LlmResponse;
import edu.study.llm_application.domain.ports.out.LlmProviderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.List;

/**
 * OpenAI adapter implementing the LLM provider port
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiLlmAdapter implements LlmProviderPort {
    
    private final WebClient webClient;
    
    @Value("${openai.api.key:demo-key}")
    private String apiKey;
    
    @Value("${openai.api.url:https://api.openai.com/v1}")
    private String apiUrl;
    
    @Value("${openai.timeout.seconds:30}")
    private int timeoutSeconds;
    
    @Override
    public LlmResponse generateResponse(LlmRequest request) throws LlmProviderException {
        log.debug("Sending request to OpenAI API for model: {}", request.getModel());
        
        try {
            // Build OpenAI request
            OpenAiDtos.ChatCompletionRequest openAiRequest = buildOpenAiRequest(request);
            
            // Call OpenAI API
            OpenAiDtos.ChatCompletionResponse response = webClient
                    .post()
                    .uri(apiUrl + "/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(openAiRequest)
                    .retrieve()
                    .bodyToMono(OpenAiDtos.ChatCompletionResponse.class)
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .block();
            
            // Convert to domain response
            return convertToDomainResponse(response, request.getId());
            
        } catch (WebClientResponseException e) {
            log.error("OpenAI API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new LlmProviderException("OpenAI API error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error calling OpenAI API: {}", e.getMessage(), e);
            throw new LlmProviderException("Failed to call OpenAI API: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean isHealthy() {
        try {
            // Simple health check - try to get models list
            return getSupportedModels().length > 0;
        } catch (Exception e) {
            log.warn("Health check failed: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public String[] getSupportedModels() {
        // For demo purposes, return commonly available models
        // In a real implementation, you might call the /models endpoint
        return new String[]{
            "gpt-3.5-turbo",
            "gpt-3.5-turbo-16k",
            "gpt-4",
            "gpt-4-turbo-preview"
        };
    }
    
    @Override
    public String getProviderName() {
        return "OpenAI";
    }
    
    private OpenAiDtos.ChatCompletionRequest buildOpenAiRequest(LlmRequest request) {
        return OpenAiDtos.ChatCompletionRequest.builder()
                .model(request.getModel())
                .messages(List.of(
                    OpenAiDtos.ChatCompletionRequest.Message.builder()
                            .role("user")
                            .content(request.getPrompt())
                            .build()
                ))
                .max_tokens(request.getMaxTokens())
                .temperature(request.getTemperature())
                .user(request.getUserId())
                .build();
    }
    
    private LlmResponse convertToDomainResponse(OpenAiDtos.ChatCompletionResponse response, String requestId) 
            throws LlmProviderException {
        
        if (response.getError() != null) {
            throw new LlmProviderException("OpenAI API returned error: " + response.getError().getMessage());
        }
        
        if (response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new LlmProviderException("OpenAI API returned no choices");
        }
        
        String content = response.getChoices().get(0).getMessage().getContent();
        Integer tokensUsed = response.getUsage() != null ? response.getUsage().getTotal_tokens() : null;
        
        return LlmResponse.success(
                requestId,
                content,
                response.getModel(),
                tokensUsed,
                null // Will be set by use case
        );
    }
}
