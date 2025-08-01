# LLM Application

A Spring Boot application for interacting with Large Language Models using Clean Architecture principles.

## Overview

This is a first implementation of an LLM application built with Java 17, Spring Boot 3.5.4, and Clean Architecture. The application provides a REST API for generating text using different LLM providers, with support for both mock and real implementations.

## Features

- 🏗️ **Clean Architecture** - Properly separated layers (Domain, Application, Infrastructure)
- 🔌 **Multiple LLM Providers** - Mock provider for development, OpenAI adapter for production
- � **Secure Configuration** - Environment variables with .env file support for API keys
- �📊 **Health Monitoring** - Health check endpoints with Actuator
- 📚 **API Documentation** - Swagger/OpenAPI 3.0 documentation
- ✅ **Input Validation** - Request validation with proper error handling
- 🔄 **Reactive HTTP Client** - WebFlux WebClient for external API calls
- 🎯 **Comprehensive Logging** - Structured logging with SLF4J
- 🔄 **Conditional Bean Loading** - Seamlessly switch between mock and real API modes

## Tech Stack

- **Java 17**
- **Spring Boot 3.5.4**
  - Spring Web
  - Spring WebFlux (for HTTP client)
  - Spring Actuator
  - Spring Validation
- **OpenAPI 3.0** (SpringDoc)
- **Environment Variables** (dotenv-java for .env file support)
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

2. Set up environment variables:
```bash
# Copy the example environment file
cp .env.example .env

# Edit .env file with your actual values (especially OPENAI_API_KEY)
# Use any text editor like nano, vim, or VS Code
code .env
```

3. Build the project:
```bash
./mvnw clean compile
```

4. Run the application:
```bash
# Option 1: Using PowerShell script (loads .env automatically)
./run-with-env.ps1

# Option 2: Using Maven directly (requires manual env variable setup)
./mvnw spring-boot:run
```

The application will start on port 8081.

### Configuration

The application uses environment variables for configuration. Create a `.env` file in the root directory (copy from `.env.example`):

```bash
# Environment variables for local development
# Copy this file to .env and fill in your actual values
# DO NOT COMMIT THE .env FILE TO VERSION CONTROL

# OpenAI Configuration
OPENAI_API_KEY=your-openai-api-key-here
OPENAI_MOCK_ENABLED=false
OPENAI_API_URL=https://api.openai.com/v1
OPENAI_TIMEOUT_SECONDS=30

# Example for mock mode (development/testing)
# OPENAI_MOCK_ENABLED=true
```

#### Security Note 🔒
- **Never commit your `.env` file to version control**
- The `.env` file is already included in `.gitignore`
- Use `.env.example` as a template for required variables
- Store production secrets in your deployment platform's secret management

#### Configuration Options

| Variable | Default | Description |
|----------|---------|-------------|
| `OPENAI_MOCK_ENABLED` | `true` | Set to `false` to use real OpenAI API |
| `OPENAI_API_KEY` | `your-openai-api-key-here` | Your OpenAI API key (required for real API) |
| `OPENAI_API_URL` | `https://api.openai.com/v1` | OpenAI API base URL |
| `OPENAI_TIMEOUT_SECONDS` | `30` | Request timeout in seconds |

#### Running the Application

**Option 1: Using PowerShell script (Recommended)**
```bash
# Automatically loads .env file and starts the application
./run-with-env.ps1
```

**Option 2: Manual environment setup**
```bash
# Set environment variables manually (Windows PowerShell)
$env:OPENAI_MOCK_ENABLED="false"
$env:OPENAI_API_KEY="your-actual-api-key"
./mvnw spring-boot:run

# Set environment variables manually (Linux/Mac)
export OPENAI_MOCK_ENABLED=false
export OPENAI_API_KEY="your-actual-api-key"
./mvnw spring-boot:run
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

```bash
# Set mock mode in .env file
OPENAI_MOCK_ENABLED=true
```

### Using Real OpenAI Provider

1. Get your OpenAI API key from [OpenAI Platform](https://platform.openai.com/api-keys)
2. Set up your `.env` file:
```bash
OPENAI_MOCK_ENABLED=false
OPENAI_API_KEY=sk-your-actual-api-key-here
```
3. Restart the application using `./run-with-env.ps1`

**Note:** Real OpenAI API usage requires billing setup in your OpenAI account.

### Environment Switching

Switch between modes easily by updating your `.env` file:

```bash
# For development/testing (free)
OPENAI_MOCK_ENABLED=true

# For production/real API testing (requires billing)
OPENAI_MOCK_ENABLED=false
OPENAI_API_KEY=your-real-api-key
```

### Adding New LLM Providers

To add a new LLM provider:

1. Implement the `LlmProviderPort` interface
2. Add the implementation in `infrastructure/adapters/llm/`
3. Configure it with `@ConditionalOnProperty` or similar

### Configuration Files

The application uses the following configuration files:

- **`.env`** - Environment variables (not committed to git)
- **`.env.example`** - Template for environment variables (committed to git)
- **`application.properties`** - Spring Boot configuration with environment variable references
- **`run-with-env.ps1`** - PowerShell script to load .env and start the application

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