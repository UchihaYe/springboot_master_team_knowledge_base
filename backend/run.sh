#!/bin/bash

echo "Starting Team Knowledge Base Backend..."
echo

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    echo "[ERROR] Maven is not installed or not in PATH"
    echo "Please install Maven 3.6+ and add it to your PATH"
    echo "See ENVIRONMENT_SETUP.md for detailed instructions"
    exit 1
fi

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "[ERROR] Java is not installed or not in PATH"
    echo "Please install Java 17+ and add it to your PATH"
    echo "See ENVIRONMENT_SETUP.md for detailed instructions"
    exit 1
fi

# Check Java version
echo "Checking Java version..."
JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "?(1\.)?\K\d+' | head -1)
if [[ $JAVA_VERSION -lt 17 ]]; then
    echo "[ERROR] Java version $JAVA_VERSION is not supported. Java 17+ is required."
    echo "Current Java version:"
    java -version
    echo
    echo "Please install Java 17 or higher."
    exit 1
fi

# Check Maven version
echo "Checking Maven version..."
mvn -version

echo
echo "[INFO] Environment check completed"
echo "[INFO] Compiling and starting the application..."
echo "[INFO] This may take a few minutes for the first run..."
echo

# Run the application
mvn spring-boot:run

echo
echo "Application stopped."