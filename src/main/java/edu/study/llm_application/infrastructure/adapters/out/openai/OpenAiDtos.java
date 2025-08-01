package edu.study.llm_application.infrastructure.adapters.out.openai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTOs for OpenAI API integration
 */
public class OpenAiDtos {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatCompletionRequest {
        private String model;
        private List<Message> messages;
        private Integer max_tokens;
        private Double temperature;
        private String user;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Message {
            private String role;
            private String content;
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatCompletionResponse {
        private String id;
        private String object;
        private Long created;
        private String model;
        private List<Choice> choices;
        private Usage usage;
        private Error error;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Choice {
            private Integer index;
            private Message message;
            private String finish_reason;
            
            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Message {
                private String role;
                private String content;
            }
        }
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Usage {
            private Integer prompt_tokens;
            private Integer completion_tokens;
            private Integer total_tokens;
        }
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Error {
            private String message;
            private String type;
            private String param;
            private String code;
        }
    }
}
