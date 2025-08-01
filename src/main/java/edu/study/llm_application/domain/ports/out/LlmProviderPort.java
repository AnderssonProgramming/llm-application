package edu.study.llm_application.domain.ports.out;

import edu.study.llm_application.domain.entities.LlmRequest;
import edu.study.llm_application.domain.entities.LlmResponse;

/**
 * Port for communicating with external LLM providers
 */
public interface LlmProviderPort {
    
    /**
     * Sends a request to the LLM provider and returns the response
     * 
     * @param request the LLM request containing prompt and configuration
     * @return the LLM response with generated content
     * @throws LlmProviderException if there's an error communicating with the provider
     */
    LlmResponse generateResponse(LlmRequest request) throws LlmProviderException;
    
    /**
     * Checks if the provider is available and healthy
     * 
     * @return true if the provider is available, false otherwise
     */
    boolean isHealthy();
    
    /**
     * Gets the supported models by this provider
     * 
     * @return array of supported model names
     */
    String[] getSupportedModels();
    
    /**
     * Gets the provider name
     * 
     * @return the name of the LLM provider
     */
    String getProviderName();
    
    /**
     * Exception thrown when there's an error with the LLM provider
     */
    class LlmProviderException extends Exception {
        public LlmProviderException(String message) {
            super(message);
        }
        
        public LlmProviderException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
