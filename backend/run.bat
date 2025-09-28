@echo off
echo Starting Team Knowledge Base Backend...
echo.

REM Check if Maven is available
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Maven is not installed or not in PATH
    echo Please install Maven 3.6+ and add it to your PATH
    echo See ENVIRONMENT_SETUP.md for detailed instructions
    pause
    exit /b 1
)

REM Check if Java is available
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed or not in PATH
    echo Please install Java 17+ and add it to your PATH
    echo See ENVIRONMENT_SETUP.md for detailed instructions
    pause
    exit /b 1
)

REM Check Java version
echo Checking Java version...
java -version 2>&1 | findstr /r "version.*\"[1-9][7-9]"
if %errorlevel% neq 0 (
    echo [WARNING] Java version might be too old. Java 17+ is required.
    echo Current Java version:
    java -version
    echo.
    echo Press any key to continue anyway, or Ctrl+C to abort...
    pause >nul
)

REM Check Maven version
echo Checking Maven version...
mvn -version

echo.
echo [INFO] Environment check completed
echo [INFO] Compiling and starting the application...
echo [INFO] This may take a few minutes for the first run...
echo.

REM Run the application
mvn spring-boot:run

echo.
echo Application stopped. Press any key to exit...
pause >nul