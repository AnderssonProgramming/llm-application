package edu.study.llm_application.application.controllers;

import edu.study.llm_application.application.dtos.HealthDto;
import edu.study.llm_application.application.dtos.LlmRequestDto;
import edu.study.llm_application.application.dtos.LlmResponseDto;
import edu.study.llm_application.application.dtos.ModelsDto;
import edu.study.llm_application.application.mappers.LlmMapper;
import edu.study.llm_application.domain.entities.LlmRequest;
import edu.study.llm_application.domain.entities.LlmResponse;
import edu.study.llm_application.domain.ports.in.LlmUseCasePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * REST Controller for LLM operations
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/llm")
@RequiredArgsConstructor
@Validated
@Tag(name = "LLM API", description = "API for interacting with Large Language Models")
public class LlmController {
    
    private final LlmUseCasePort llmUseCase;
    private final LlmMapper mapper;
    private final BuildProperties buildProperties;
    
    @Operation(summary = "Generate text using LLM", 
               description = "Sends a prompt to the configured LLM and returns the generated response")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                     description = "Text generated successfully",
                     content = @Content(mediaType = "application/json",
                                      schema = @Schema(implementation = LlmResponseDto.class))),
        @ApiResponse(responseCode = "400", 
                     description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", 
                     description = "Internal server error or LLM provider error")
    })
    @PostMapping("/generate")
    public ResponseEntity<LlmResponseDto> generateText(
            @Parameter(description = "LLM request containing prompt and configuration")
            @Valid @RequestBody LlmRequestDto requestDto) {
        
        log.info("Received LLM generation request with model: {}", requestDto.getModel());
        
        try {
            // Convert DTO to domain entity
            LlmRequest domainRequest = mapper.toDomain(requestDto);
            
            // Process request through use case
            LlmResponse domainResponse = llmUseCase.processRequest(domainRequest);
            
            // Convert domain response to DTO
            LlmResponseDto responseDto = mapper.toDto(domainResponse);
            
            return ResponseEntity.ok(responseDto);
            
        } catch (LlmUseCasePort.LlmProcessingException e) {
            log.error("Error processing LLM request: {}", e.getMessage());
            
            LlmResponseDto errorResponse = LlmResponseDto.builder()
                    .status("ERROR")
                    .errorMessage(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
    @Operation(summary = "Get available models", 
               description = "Returns a list of all available LLM models")
    @ApiResponse(responseCode = "200", 
                 description = "Models retrieved successfully",
                 content = @Content(mediaType = "application/json",
                                  schema = @Schema(implementation = ModelsDto.class)))
    @GetMapping("/models")
    public ResponseEntity<ModelsDto> getAvailableModels() {
        log.debug("Retrieving available models");
        
        String[] models = llmUseCase.getAvailableModels();
        
        ModelsDto response = ModelsDto.builder()
                .models(models)
                .defaultModel("gpt-3.5-turbo")
                .count(models.length)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Health check", 
               description = "Returns the health status of the LLM service")
    @ApiResponse(responseCode = "200", 
                 description = "Health status retrieved successfully",
                 content = @Content(mediaType = "application/json",
                                  schema = @Schema(implementation = HealthDto.class)))
    @GetMapping("/health")
    public ResponseEntity<HealthDto> getHealth() {
        log.debug("Checking service health");
        
        boolean isHealthy = llmUseCase.isServiceHealthy();
        
        HealthDto response = HealthDto.builder()
                .status(isHealthy ? "HEALTHY" : "UNHEALTHY")
                .llmProviderHealthy(isHealthy)
                .version(buildProperties != null ? buildProperties.getVersion() : "unknown")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
        
        return ResponseEntity.ok(response);
    }
}
