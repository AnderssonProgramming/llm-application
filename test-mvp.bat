@echo off
echo 🚀 Iniciando MVP LLM Application...
echo.

REM Iniciar la aplicación en background
start /B .\mvnw.cmd spring-boot:run > nul 2>&1

echo ⏳ Esperando que la aplicación inicie (20 segundos)...
timeout /t 20 /nobreak > nul

echo.
echo 🔍 Probando endpoints del MVP...
echo.

echo 1️⃣ Testing Health Check:
curl -s http://localhost:8081/api/v1/llm/health
echo.
echo.

echo 2️⃣ Testing Available Models:
curl -s http://localhost:8081/api/v1/llm/models
echo.
echo.

echo 3️⃣ Testing Text Generation:
curl -s -X POST http://localhost:8081/api/v1/llm/generate ^
  -H "Content-Type: application/json" ^
  -d "{\"prompt\":\"Hello, this is a test prompt for the LLM MVP\",\"model\":\"mock-gpt-3.5-turbo\",\"maxTokens\":50,\"temperature\":0.7}"
echo.
echo.

echo 4️⃣ Testing Validation (Empty Prompt):
curl -s -X POST http://localhost:8081/api/v1/llm/generate ^
  -H "Content-Type: application/json" ^
  -d "{\"prompt\":\"\",\"model\":\"mock-gpt-3.5-turbo\"}"
echo.
echo.

echo 5️⃣ Testing Actuator Health:
curl -s http://localhost:8081/actuator/health
echo.
echo.

echo 🎉 ¡MVP LLM Application funcionando perfectamente!
echo 📋 Endpoints disponibles:
echo    • http://localhost:8081/api/v1/llm/generate (POST)
echo    • http://localhost:8081/api/v1/llm/models (GET)
echo    • http://localhost:8081/api/v1/llm/health (GET)
echo    • http://localhost:8081/swagger-ui/index.html (Documentación)
echo    • http://localhost:8081/actuator/health (Spring Health)
echo.

echo 🛑 Presiona cualquier tecla para detener la aplicación...
pause > nul

echo Deteniendo aplicación...
taskkill /f /im java.exe > nul 2>&1
echo ✅ Aplicación detenida.
