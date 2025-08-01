package edu.study.llm_application.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * Request DTO for LLM API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for LLM text generation")
public class LlmRequestDto {
    
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 10000, message = "Prompt cannot exceed 10000 characters")
    @Schema(description = "The text prompt to send to the LLM", 
            example = "Explain what is artificial intelligence", 
            required = true)
    private String prompt;
    
    @Schema(description = "The LLM model to use", 
            example = "gpt-3.5-turbo", 
            defaultValue = "gpt-3.5-turbo")
    private String model;
    
    @Min(value = 1, message = "Max tokens must be at least 1")
    @Max(value = 4000, message = "Max tokens cannot exceed 4000")
    @Schema(description = "Maximum number of tokens to generate", 
            example = "150", 
            minimum = "1", 
            maximum = "4000")
    @JsonProperty("max_tokens")
    private Integer maxTokens;
    
    @DecimalMin(value = "0.0", message = "Temperature must be between 0.0 and 2.0")
    @DecimalMax(value = "2.0", message = "Temperature must be between 0.0 and 2.0")
    @Schema(description = "Sampling temperature (0.0 = deterministic, 2.0 = very creative)", 
            example = "0.7", 
            minimum = "0.0", 
            maximum = "2.0")
    private Double temperature;
    
    @Schema(description = "Optional user identifier", 
            example = "user123")
    @JsonProperty("user_id")
    private String userId;
    
    @Schema(description = "Optional context or conversation history")
    private List<String> context;
}
