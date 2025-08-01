package edu.study.llm_application.application.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for LLM API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response object containing the LLM generated text")
public class LlmResponseDto {
    
    @Schema(description = "Unique identifier for this response", 
            example = "resp_123456789")
    private String id;
    
    @Schema(description = "Identifier of the original request", 
            example = "req_123456789")
    @JsonProperty("request_id")
    private String requestId;
    
    @Schema(description = "The generated text content", 
            example = "Artificial intelligence (AI) refers to the simulation of human intelligence...")
    private String content;
    
    @Schema(description = "The model used for generation", 
            example = "gpt-3.5-turbo")
    private String model;
    
    @Schema(description = "Number of tokens used in the response", 
            example = "85")
    @JsonProperty("tokens_used")
    private Integer tokensUsed;
    
    @Schema(description = "Processing time in milliseconds", 
            example = "1250.5")
    @JsonProperty("processing_time_ms")
    private Double processingTimeMs;
    
    @Schema(description = "Timestamp when the response was generated")
    private LocalDateTime timestamp;
    
    @Schema(description = "Response status", 
            example = "SUCCESS")
    private String status;
    
    @Schema(description = "Error message if status is ERROR")
    @JsonProperty("error_message")
    private String errorMessage;
}
