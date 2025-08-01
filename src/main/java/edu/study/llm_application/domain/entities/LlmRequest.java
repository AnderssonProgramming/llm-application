package edu.study.llm_application.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain entity representing an LLM request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LlmRequest {
    
    private String id;
    private String prompt;
    private String model;
    private Integer maxTokens;
    private Double temperature;
    private String userId;
    private LocalDateTime timestamp;
    private List<String> context;
    
    /**
     * Validates if the request has minimum required fields
     */
    public boolean isValid() {
        return prompt != null && !prompt.trim().isEmpty() && 
               model != null && !model.trim().isEmpty();
    }
    
    /**
     * Sets default values for optional parameters
     */
    public LlmRequest withDefaults() {
        return LlmRequest.builder()
                .id(this.id)
                .prompt(this.prompt)
                .model(this.model != null ? this.model : "gpt-3.5-turbo")
                .maxTokens(this.maxTokens != null ? this.maxTokens : 150)
                .temperature(this.temperature != null ? this.temperature : 0.7)
                .userId(this.userId)
                .timestamp(this.timestamp != null ? this.timestamp : LocalDateTime.now())
                .context(this.context)
                .build();
    }
}
