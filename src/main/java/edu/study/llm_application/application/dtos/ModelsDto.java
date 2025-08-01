package edu.study.llm_application.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for available models information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Information about available LLM models")
public class ModelsDto {
    
    @Schema(description = "List of available model names", 
            example = "[\"gpt-3.5-turbo\", \"gpt-4\", \"mock-model-v1\"]")
    private String[] models;
    
    @Schema(description = "Default model used when none is specified", 
            example = "gpt-3.5-turbo")
    private String defaultModel;
    
    @Schema(description = "Total number of available models", 
            example = "3")
    private int count;
}
