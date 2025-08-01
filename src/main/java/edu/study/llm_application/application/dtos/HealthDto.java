package edu.study.llm_application.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for system health information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "System health status information")
public class HealthDto {
    
    @Schema(description = "Overall system status", 
            example = "HEALTHY")
    private String status;
    
    @Schema(description = "LLM provider availability", 
            example = "true")
    private boolean llmProviderHealthy;
    
    @Schema(description = "Application version", 
            example = "1.0.0")
    private String version;
    
    @Schema(description = "Current timestamp", 
            example = "2024-01-15T10:30:00")
    private String timestamp;
}
