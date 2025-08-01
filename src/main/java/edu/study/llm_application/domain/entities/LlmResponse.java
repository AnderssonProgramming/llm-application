package edu.study.llm_application.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain entity representing an LLM response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LlmResponse {
    
    private String id;
    private String requestId;
    private String content;
    private String model;
    private Integer tokensUsed;
    private Double processingTimeMs;
    private LocalDateTime timestamp;
    private String status;
    private String errorMessage;
    
    /**
     * Creates a successful response
     */
    public static LlmResponse success(String requestId, String content, String model, 
                                    Integer tokensUsed, Double processingTimeMs) {
        return LlmResponse.builder()
                .requestId(requestId)
                .content(content)
                .model(model)
                .tokensUsed(tokensUsed)
                .processingTimeMs(processingTimeMs)
                .timestamp(LocalDateTime.now())
                .status("SUCCESS")
                .build();
    }
    
    /**
     * Creates an error response
     */
    public static LlmResponse error(String requestId, String errorMessage) {
        return LlmResponse.builder()
                .requestId(requestId)
                .errorMessage(errorMessage)
                .timestamp(LocalDateTime.now())
                .status("ERROR")
                .build();
    }
    
    /**
     * Checks if the response was successful
     */
    public boolean isSuccessful() {
        return "SUCCESS".equals(status) && content != null && !content.trim().isEmpty();
    }
}
