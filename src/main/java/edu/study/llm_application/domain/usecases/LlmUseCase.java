package edu.study.llm_application.domain.usecases;

import edu.study.llm_application.domain.entities.LlmRequest;
import edu.study.llm_application.domain.entities.LlmResponse;
import edu.study.llm_application.domain.ports.in.LlmUseCasePort;
import edu.study.llm_application.domain.ports.out.LlmProviderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of LLM use case containing business logic
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LlmUseCase implements LlmUseCasePort {
    
    private final LlmProviderPort llmProviderPort;
    
    @Override
    public LlmResponse processRequest(LlmRequest request) throws LlmProcessingException {
        log.info("Processing LLM request for prompt: {}", 
                request.getPrompt().substring(0, Math.min(50, request.getPrompt().length())));
        
        // Validate request
        if (!validateRequest(request)) {
            throw new LlmProcessingException("Invalid request: missing required fields");
        }
        
        try {
            // Set request ID if not present
            if (request.getId() == null) {
                request.setId(UUID.randomUUID().toString());
            }
            
            // Apply defaults
            LlmRequest processedRequest = request.withDefaults();
            
            // Validate model is supported
            String[] supportedModels = llmProviderPort.getSupportedModels();
            boolean modelSupported = false;
            for (String model : supportedModels) {
                if (model.equals(processedRequest.getModel())) {
                    modelSupported = true;
                    break;
                }
            }
            
            if (!modelSupported) {
                throw new LlmProcessingException(
                    "Model '" + processedRequest.getModel() + "' is not supported");
            }
            
            // Call LLM provider
            long startTime = System.currentTimeMillis();
            LlmResponse response = llmProviderPort.generateResponse(processedRequest);
            long endTime = System.currentTimeMillis();
            
            // Set processing time
            response.setProcessingTimeMs((double) (endTime - startTime));
            response.setId(UUID.randomUUID().toString());
            
            log.info("Successfully processed LLM request {} in {} ms", 
                    request.getId(), response.getProcessingTimeMs());
            
            return response;
            
        } catch (LlmProviderPort.LlmProviderException e) {
            log.error("Error processing LLM request: {}", e.getMessage(), e);
            throw new LlmProcessingException("Failed to process request: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean validateRequest(LlmRequest request) {
        if (request == null) {
            return false;
        }
        
        // Check required fields
        if (request.getPrompt() == null || request.getPrompt().trim().isEmpty()) {
            log.warn("Request validation failed: prompt is empty");
            return false;
        }
        
        // Check prompt length (reasonable limit)
        if (request.getPrompt().length() > 10000) {
            log.warn("Request validation failed: prompt too long ({} characters)", 
                    request.getPrompt().length());
            return false;
        }
        
        // Check parameter ranges
        if (request.getMaxTokens() != null && (request.getMaxTokens() < 1 || request.getMaxTokens() > 4000)) {
            log.warn("Request validation failed: invalid maxTokens value: {}", request.getMaxTokens());
            return false;
        }
        
        if (request.getTemperature() != null && (request.getTemperature() < 0.0 || request.getTemperature() > 2.0)) {
            log.warn("Request validation failed: invalid temperature value: {}", request.getTemperature());
            return false;
        }
        
        return true;
    }
    
    @Override
    public String[] getAvailableModels() {
        try {
            return llmProviderPort.getSupportedModels();
        } catch (Exception e) {
            log.error("Error getting available models: {}", e.getMessage());
            return new String[]{"gpt-3.5-turbo"}; // fallback
        }
    }
    
    @Override
    public boolean isServiceHealthy() {
        try {
            return llmProviderPort.isHealthy();
        } catch (Exception e) {
            log.error("Error checking service health: {}", e.getMessage());
            return false;
        }
    }
}
