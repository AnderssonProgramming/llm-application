# Script para probar el MVP de la aplicación LLM
Write-Host "🚀 Iniciando MVP LLM Application..." -ForegroundColor Green

# Iniciar la aplicación en background
$app = Start-Process -FilePath ".\mvnw.cmd" -ArgumentList "spring-boot:run" -PassThru -WindowStyle Hidden

# Esperar a que la aplicación inicie
Write-Host "⏳ Esperando que la aplicación inicie (15 segundos)..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

try {
    Write-Host "`n🔍 Probando endpoints del MVP..." -ForegroundColor Green
    
    # 1. Probar Health Check
    Write-Host "`n1️⃣ Testing Health Check:" -ForegroundColor Cyan
    $healthResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/llm/health" -Method GET
    Write-Host "✅ Health Status: $($healthResponse.status)" -ForegroundColor Green
    
    # 2. Probar lista de modelos
    Write-Host "`n2️⃣ Testing Available Models:" -ForegroundColor Cyan
    $modelsResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/llm/models" -Method GET
    Write-Host "✅ Available Models:" -ForegroundColor Green
    $modelsResponse.models | ForEach-Object { Write-Host "   - $_" -ForegroundColor White }
    
    # 3. Probar generación de texto
    Write-Host "`n3️⃣ Testing Text Generation:" -ForegroundColor Cyan
    $requestBody = @{
        prompt = "Hello, this is a test prompt for the LLM MVP"
        model = "mock-gpt-3.5-turbo"
        maxTokens = 50
        temperature = 0.7
    } | ConvertTo-Json
    
    $headers = @{
        'Content-Type' = 'application/json'
    }
    
    $generateResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/llm/generate" -Method POST -Body $requestBody -Headers $headers
    Write-Host "✅ Generated Text:" -ForegroundColor Green
    Write-Host "   Prompt: $($generateResponse.prompt)" -ForegroundColor White
    Write-Host "   Response: $($generateResponse.generatedText)" -ForegroundColor White
    Write-Host "   Model: $($generateResponse.model)" -ForegroundColor White
    Write-Host "   Tokens: $($generateResponse.tokensUsed)" -ForegroundColor White
    
    # 4. Probar validación de errores
    Write-Host "`n4️⃣ Testing Validation (Empty Prompt):" -ForegroundColor Cyan
    $invalidRequestBody = @{
        prompt = ""
        model = "mock-gpt-3.5-turbo"
    } | ConvertTo-Json
    
    try {
        $errorResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/llm/generate" -Method POST -Body $invalidRequestBody -Headers $headers -ErrorAction Stop
    }
    catch {
        $errorDetails = $_.ErrorDetails.Message | ConvertFrom-Json
        Write-Host "✅ Validation Error Handled:" -ForegroundColor Green
        Write-Host "   Error: $($errorDetails.message)" -ForegroundColor White
    }
    
    # 5. Probar Swagger UI
    Write-Host "`n5️⃣ Testing Swagger UI:" -ForegroundColor Cyan
    try {
        $swaggerResponse = Invoke-WebRequest -Uri "http://localhost:8081/swagger-ui/index.html" -Method GET
        Write-Host "✅ Swagger UI disponible en: http://localhost:8081/swagger-ui/index.html" -ForegroundColor Green
    }
    catch {
        Write-Host "❌ Error accessing Swagger UI: $($_.Exception.Message)" -ForegroundColor Red
    }
    
    # 6. Probar Actuator Health
    Write-Host "`n6️⃣ Testing Actuator Health:" -ForegroundColor Cyan
    $actuatorResponse = Invoke-RestMethod -Uri "http://localhost:8081/actuator/health" -Method GET
    Write-Host "✅ Actuator Health: $($actuatorResponse.status)" -ForegroundColor Green
    
    Write-Host "`n🎉 ¡MVP LLM Application funcionando perfectamente!" -ForegroundColor Green
    Write-Host "📋 Endpoints disponibles:" -ForegroundColor Yellow
    Write-Host "   • http://localhost:8081/api/v1/llm/generate (POST)" -ForegroundColor White
    Write-Host "   • http://localhost:8081/api/v1/llm/models (GET)" -ForegroundColor White
    Write-Host "   • http://localhost:8081/api/v1/llm/health (GET)" -ForegroundColor White
    Write-Host "   • http://localhost:8081/swagger-ui/index.html (Documentación)" -ForegroundColor White
    Write-Host "   • http://localhost:8081/actuator/health (Spring Health)" -ForegroundColor White
    
}
catch {
    Write-Host "❌ Error during testing: $($_.Exception.Message)" -ForegroundColor Red
}
finally {
    # Detener la aplicación
    Write-Host "`n🛑 Deteniendo aplicación..." -ForegroundColor Yellow
    Stop-Process -Id $app.Id -Force -ErrorAction SilentlyContinue
    Write-Host "✅ Aplicación detenida." -ForegroundColor Green
}
