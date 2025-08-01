# LLM Application

A Spring Boot application for interacting with Large Language Models using Clean Architecture principles.

## Overview

This LLM application provides a REST API for generating text using different providers (mock for development, OpenAI for production). Built with Java 17, Spring Boot 3.5.4, and Clean Architecture patterns.

## Features

- 🏗️ **Clean Architecture** - Separated layers (Domain, Application, Infrastructure)
- 🔌 **Multiple LLM Providers** - Mock and OpenAI adapters with conditional loading
- 🔒 **Secure Configuration** - Environment variables with .env file support
- 📊 **Health Monitoring** - Actuator endpoints for monitoring
- 📚 **API Documentation** - Swagger/OpenAPI 3.0 with interactive UI
- ✅ **Input Validation** - Request validation with comprehensive error handling
- 🔄 **Reactive HTTP Client** - WebFlux WebClient for external API calls

## Tech Stack

- **Java 17** + **Spring Boot 3.5.4** (Web, WebFlux, Actuator, Validation)
- **OpenAPI 3.0** (SpringDoc) + **Lombok** + **dotenv-java** + **Maven**

## Architecture

```
src/main/java/edu/study/llm_application/
├── domain/                 # Domain Layer (Business Logic)
│   ├── entities/          # Domain Entities
│   ├── ports/             # Interfaces (in/out)
│   └── usecases/         # Business Logic Implementation
├── application/           # Application Layer (API)
│   ├── controllers/      # REST Controllers
│   ├── dtos/            # Data Transfer Objects
│   └── mappers/         # DTO ↔ Domain Mapping
└── infrastructure/       # Infrastructure Layer
    ├── adapters/        # External Service Adapters
    └── config/         # Configuration Classes
```

## Quick Start

### Prerequisites
- Java 17+ and Maven 3.6+

### Setup

1. **Clone and configure**:
```bash
git clone https://github.com/AnderssonProgramming/llm-application.git
cd llm-application
cp .env.example .env
```

2. **Edit `.env` file** with your configuration:
```bash
# For development (free, no API key needed)
OPENAI_MOCK_ENABLED=true

# For production (requires OpenAI API key)
OPENAI_MOCK_ENABLED=false
OPENAI_API_KEY=sk-your-actual-api-key-here
OPENAI_API_URL=https://api.openai.com/v1
OPENAI_TIMEOUT_SECONDS=30
```

3. **Run the application**:
```bash
./mvnw spring-boot:run
```

The application starts on http://localhost:8081

### Configuration

| Variable | Default | Description |
|----------|---------|-------------|
| `OPENAI_MOCK_ENABLED` | `true` | `false` = real OpenAI API, `true` = mock responses |
| `OPENAI_API_KEY` | `your-openai-api-key-here` | Your OpenAI API key (required when mock=false) |
| `OPENAI_API_URL` | `https://api.openai.com/v1` | OpenAI API base URL |
| `OPENAI_TIMEOUT_SECONDS` | `30` | Request timeout in seconds |

🔒 **Security**: Never commit your `.env` file. It's already in `.gitignore`.

## API Endpoints

### Generate Text
```http
POST /api/v1/llm/generate
Content-Type: application/json

{
  "prompt": "Explain artificial intelligence",
  "model": "gpt-3.5-turbo",
  "max_tokens": 150,
  "temperature": 0.7
}
```

### Other Endpoints
- **Models**: `GET /api/v1/llm/models`
- **Health**: `GET /api/v1/llm/health`
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **Actuator**: http://localhost:8081/actuator/health

## Development

### Switching Between Providers

**Mock Provider** (default, free):
```bash
# In .env file
OPENAI_MOCK_ENABLED=true
```

**OpenAI Provider** (requires API key):
```bash
# In .env file
OPENAI_MOCK_ENABLED=false
OPENAI_API_KEY=sk-your-actual-api-key-here
```

Get your API key from [OpenAI Platform](https://platform.openai.com/api-keys). Note: Real API usage requires billing setup.

### Testing
```bash
./mvnw test
```

### Adding New Providers

1. Implement `LlmProviderPort` interface
2. Add implementation in `infrastructure/adapters/llm/`
3. Configure with `@ConditionalOnProperty`

## Example Usage

```bash
# Generate text
curl -X POST http://localhost:8081/api/v1/llm/generate \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Write a haiku about coding", "model": "gpt-3.5-turbo"}'

# Get models
curl http://localhost:8081/api/v1/llm/models

# Health check
curl http://localhost:8081/api/v1/llm/health
```

## Error Handling

- **400 Bad Request** - Validation errors with detailed field information
- **500 Internal Server Error** - LLM provider errors with descriptive messages
- **Standardized Error Format** - Consistent error response structure

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.