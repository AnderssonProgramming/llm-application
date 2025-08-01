package edu.study.llm_application.infrastructure.config;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.Properties;

/**
 * Additional configuration for the application
 */
@Configuration
public class ApplicationConfig {
    
    /**
     * Provides BuildProperties bean when not available (e.g., during development)
     */
    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public BuildProperties buildProperties() {
        Properties properties = new Properties();
        properties.setProperty("version", "1.0.0-SNAPSHOT");
        properties.setProperty("group", "edu.study");
        properties.setProperty("artifact", "llm-application");
        properties.setProperty("name", "LLM Application");
        properties.setProperty("time", Instant.now().toString());
        
        return new BuildProperties(properties);
    }
}
