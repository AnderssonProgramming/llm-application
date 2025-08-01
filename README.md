# LLM Application

A Spring Boot application for interacting with Large Language Models using Clean Architecture principles.

## Overview

This is a first implementation of an LLM application built with Java 17, Spring Boot 3.5.4, and Clean Architecture. The application provides a REST API for generating text using different LLM providers, with support for both mock and real implementations.

## Features

- 🏗️ **Clean Architecture** - Properly separated layers (Domain, Application, Infrastructure)
- 🔌 **Multiple LLM Providers** - Mock provider for development, OpenAI adapter for production
- 📊 **Health Monitoring** - Health check endpoints with Actuator
- 📚 **API Documentation** - Swagger/OpenAPI 3.0 documentation
- ✅ **Input Validation** - Request validation with proper error handling
- 🔄 **Reactive HTTP Client** - WebFlux WebClient for external API calls
- 🎯 **Comprehensive Logging** - Structured logging with SLF4J

## Tech Stack

- **Java 17**
- **Spring Boot 3.5.4**
  - Spring Web
  - Spring WebFlux (for HTTP client)
  - Spring Actuator
  - Spring Validation
- **OpenAPI 3.0** (SpringDoc)
- **Lombok** (for reducing boilerplate)
- **Maven** (build tool)

## Architecture

The application follows Clean Architecture principles:

```
src/main/java/edu/study/llm_application/
├── domain/                 # Domain Layer (Business Logic)
│   ├── entities/          # Domain Entities
│   ├── ports/             # Interfaces
│   │   ├── in/           # Input Ports (Use Cases)
│   │   └── out/          # Output Ports (Gateways)
│   └── usecases/         # Business Logic Implementation
├── application/           # Application Layer (API)
│   ├── controllers/      # REST Controllers
│   ├── dtos/            # Data Transfer Objects
│   └── mappers/         # DTO ↔ Domain Mapping
└── infrastructure/       # Infrastructure Layer
    ├── adapters/        # External Service Adapters
    │   └── llm/        # LLM Provider Implementations
    └── config/         # Configuration Classes
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/AnderssonProgramming/llm-application.git
cd llm-application
```

2. Build the project:
```bash
./mvnw clean compile
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on port 8081.

### Configuration

The application can be configured via `application.properties`:

```properties
# Server Configuration
server.port=8081

# LLM Provider Configuration
openai.mock.enabled=true           # Set to false to use real OpenAI
openai.api.key=your-openai-api-key-here
openai.api.url=https://api.openai.com/v1
openai.timeout.seconds=30

# Documentation
springdoc.swagger-ui.path=/swagger-ui.html
```

## API Endpoints

### Generate Text
```http
POST /api/v1/llm/generate
Content-Type: application/json

{
  "prompt": "Explain what is artificial intelligence",
  "model": "gpt-3.5-turbo",
  "max_tokens": 150,
  "temperature": 0.7,
  "user_id": "user123"
}
```

### Get Available Models
```http
GET /api/v1/llm/models
```

### Health Check
```http
GET /api/v1/llm/health
```

### API Documentation
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/api-docs`

### Actuator Endpoints
- Health: `http://localhost:8081/actuator/health`
- Info: `http://localhost:8081/actuator/info`
- Metrics: `http://localhost:8081/actuator/metrics`

## Development

### Using Mock Provider (Default)

The application comes with a mock LLM provider that simulates responses without making external API calls. This is perfect for development and testing.

### Using Real OpenAI Provider

1. Set `openai.mock.enabled=false` in `application.properties`
2. Add your OpenAI API key: `openai.api.key=sk-your-actual-key`
3. Restart the application

### Adding New LLM Providers

To add a new LLM provider:

1. Implement the `LlmProviderPort` interface
2. Add the implementation in `infrastructure/adapters/llm/`
3. Configure it with `@ConditionalOnProperty` or similar

## Testing

Run tests with:
```bash
./mvnw test
```

## Example Usage

### Using curl

```bash
# Generate text
curl -X POST http://localhost:8081/api/v1/llm/generate \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Write a short poem about technology",
    "model": "mock-gpt-3.5-turbo",
    "max_tokens": 100,
    "temperature": 0.8
  }'

# Get available models
curl http://localhost:8081/api/v1/llm/models

# Check health
curl http://localhost:8081/api/v1/llm/health
```

## Error Handling

The application includes comprehensive error handling:

- **Validation Errors** - 400 Bad Request with detailed field errors
- **LLM Provider Errors** - 500 Internal Server Error with error message
- **General Errors** - Standardized error response format

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Spring Boot community for excellent documentation
- Clean Architecture principles by Robert C. Martin
- OpenAI for providing the LLM API reference