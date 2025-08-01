package edu.study.llm_application.application.mappers;

import edu.study.llm_application.application.dtos.LlmRequestDto;
import edu.study.llm_application.application.dtos.LlmResponseDto;
import edu.study.llm_application.domain.entities.LlmRequest;
import edu.study.llm_application.domain.entities.LlmResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between DTOs and domain entities
 */
@Component
public class LlmMapper {
    
    /**
     * Converts LLM request DTO to domain entity
     */
    public LlmRequest toDomain(LlmRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        return LlmRequest.builder()
                .prompt(dto.getPrompt())
                .model(dto.getModel())
                .maxTokens(dto.getMaxTokens())
                .temperature(dto.getTemperature())
                .userId(dto.getUserId())
                .context(dto.getContext())
                .build();
    }
    
    /**
     * Converts LLM response domain entity to DTO
     */
    public LlmResponseDto toDto(LlmResponse domain) {
        if (domain == null) {
            return null;
        }
        
        return LlmResponseDto.builder()
                .id(domain.getId())
                .requestId(domain.getRequestId())
                .content(domain.getContent())
                .model(domain.getModel())
                .tokensUsed(domain.getTokensUsed())
                .processingTimeMs(domain.getProcessingTimeMs())
                .timestamp(domain.getTimestamp())
                .status(domain.getStatus())
                .errorMessage(domain.getErrorMessage())
                .build();
    }
}
