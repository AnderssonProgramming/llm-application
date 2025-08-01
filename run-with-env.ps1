# Load environment variables from .env file and run the application
param(
    [string]$Command = "spring-boot:run"
)

Write-Host "Loading environment variables from .env file..." -ForegroundColor Green

# Check if .env file exists
if (Test-Path ".env") {
    # Read .env file and set environment variables
    Get-Content ".env" | ForEach-Object {
        if ($_ -match "^([^#=]+)=(.*)$") {
            $name = $matches[1].Trim()
            $value = $matches[2].Trim()
            
            # Remove quotes if present
            $value = $value -replace '^"(.*)"$', '$1'
            $value = $value -replace "^'(.*)'$", '$1'
            
            [Environment]::SetEnvironmentVariable($name, $value, "Process")
            Write-Host "Set $name" -ForegroundColor Yellow
        }
    }
    
    Write-Host "Environment variables loaded successfully!" -ForegroundColor Green
    Write-Host "Starting Spring Boot application..." -ForegroundColor Green
    
    # Run Maven command with environment variables
    & .\mvnw.cmd $Command
} else {
    Write-Host "Error: .env file not found!" -ForegroundColor Red
    Write-Host "Please create a .env file with your OpenAI API key" -ForegroundColor Red
    exit 1
}
