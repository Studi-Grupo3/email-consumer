#!/bin/bash
# Helper script for common development tasks

set -e

function print_usage() {
    echo "Usage: ./scripts.sh [command]"
    echo ""
    echo "Available commands:"
    echo "  build              - Build the project"
    echo "  test               - Run tests"
    echo "  run                - Run the application locally"
    echo "  rabbitmq-start     - Start RabbitMQ using Docker"
    echo "  rabbitmq-stop      - Stop RabbitMQ Docker container"
    echo "  send-test-email    - Send a test email message"
    echo "  docker-build       - Build Docker image"
    echo "  docker-up          - Start all services with Docker Compose"
    echo "  docker-down        - Stop all Docker Compose services"
    echo "  clean              - Clean build artifacts"
}

function build() {
    echo "Building the project..."
    mvn clean package -DskipTests
}

function test() {
    echo "Running tests..."
    mvn test
}

function run() {
    echo "Running the application..."
    mvn spring-boot:run
}

function rabbitmq_start() {
    echo "Starting RabbitMQ..."
    docker-compose -f docker-compose-rabbitmq-only.yml up -d
    echo "RabbitMQ started. Management UI available at http://localhost:15672"
    echo "Username: guest, Password: guest"
}

function rabbitmq_stop() {
    echo "Stopping RabbitMQ..."
    docker-compose -f docker-compose-rabbitmq-only.yml down
}

function send_test_email() {
    echo "Sending test email..."
    curl -X POST http://localhost:8080/api/v1/email/send \
      -H "Content-Type: application/json" \
      -d @sample-email-request.json
    echo ""
}

function docker_build() {
    echo "Building Docker image..."
    docker build -t email-consumer:latest .
}

function docker_up() {
    echo "Starting all services with Docker Compose..."
    docker-compose up -d
    echo "Services started. Application available at http://localhost:8080"
    echo "RabbitMQ Management UI available at http://localhost:15672"
}

function docker_down() {
    echo "Stopping all Docker Compose services..."
    docker-compose down
}

function clean() {
    echo "Cleaning build artifacts..."
    mvn clean
}

# Main script logic
case "$1" in
    build)
        build
        ;;
    test)
        test
        ;;
    run)
        run
        ;;
    rabbitmq-start)
        rabbitmq_start
        ;;
    rabbitmq-stop)
        rabbitmq_stop
        ;;
    send-test-email)
        send_test_email
        ;;
    docker-build)
        docker_build
        ;;
    docker-up)
        docker_up
        ;;
    docker-down)
        docker_down
        ;;
    clean)
        clean
        ;;
    *)
        print_usage
        exit 1
        ;;
esac
