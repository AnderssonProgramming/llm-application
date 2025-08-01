package edu.study.llm_application.domain.ports.in;

import edu.study.llm_application.domain.entities.LlmRequest;
import edu.study.llm_application.domain.entities.LlmResponse;

/**
 * Use case port for LLM operations
 */
public interface LlmUseCasePort {
    
    /**
     * Processes an LLM request and returns the generated response
     * 
     * @param request the LLM request to process
     * @return the generated response
     * @throws IllegalArgumentException if the request is invalid
     * @throws LlmProcessingException if there's an error processing the request
     */
    LlmResponse processRequest(LlmRequest request) throws LlmProcessingException;
    
    /**
     * Validates if an LLM request is properly formatted
     * 
     * @param request the request to validate
     * @return true if valid, false otherwise
     */
    boolean validateRequest(LlmRequest request);
    
    /**
     * Gets information about available models
     * 
     * @return array of available model names
     */
    String[] getAvailableModels();
    
    /**
     * Checks the health status of the LLM service
     * 
     * @return true if the service is healthy, false otherwise
     */
    boolean isServiceHealthy();
    
    /**
     * Exception thrown when there's an error processing an LLM request
     */
    class LlmProcessingException extends Exception {
        public LlmProcessingException(String message) {
            super(message);
        }
        
        public LlmProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
