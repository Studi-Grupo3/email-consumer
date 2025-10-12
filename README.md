# Email Consumer

A Spring Boot application that consumes email messages from RabbitMQ queue.

## Overview

This is a RabbitMQ consumer service built with Spring Boot and Spring AMQP. It listens to a RabbitMQ queue for email messages and processes them.

## Technologies Used

- Java 17
- Spring Boot 3.1.5
- Spring AMQP (RabbitMQ)
- Maven
- Docker & Docker Compose
- Lombok

## Project Structure

```
email-consumer/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/studi/grupo3/emailconsumer/
│   │   │       ├── EmailConsumerApplication.java    # Main application class
│   │   │       ├── config/
│   │   │       │   └── RabbitMQConfig.java          # RabbitMQ configuration
│   │   │       ├── consumer/
│   │   │       │   └── EmailConsumer.java           # Message consumer
│   │   │       ├── controller/
│   │   │       │   └── EmailTestController.java     # Test REST controller
│   │   │       ├── model/
│   │   │       │   └── EmailMessage.java            # Email message DTO
│   │   │       └── service/
│   │   │           └── EmailProducerService.java    # Producer service (for testing)
│   │   └── resources/
│   │       └── application.properties               # Application configuration
│   └── test/
│       └── java/
├── pom.xml                                          # Maven dependencies
├── Dockerfile                                       # Docker image configuration
└── docker-compose.yml                               # Docker Compose setup
```

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker and Docker Compose (for containerized deployment)

## Configuration

The application can be configured via `src/main/resources/application.properties`:

### RabbitMQ Configuration
```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

### Queue Configuration
```properties
rabbitmq.queue.email=email.queue
rabbitmq.exchange.email=email.exchange
rabbitmq.routing.key.email=email.routing.key
```

## Running the Application

### Option 1: Using Docker Compose (Recommended)

This will start both RabbitMQ and the Email Consumer application:

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f email-consumer

# Stop services
docker-compose down
```

### Option 2: Running Locally

1. **Start RabbitMQ** (using Docker):
```bash
docker run -d --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3.12-management
```

2. **Build the application**:
```bash
mvn clean package
```

3. **Run the application**:
```bash
mvn spring-boot:run
```

Or run the jar file:
```bash
java -jar target/email-consumer-1.0.0.jar
```

## Testing the Application

### Access RabbitMQ Management UI
Open your browser and navigate to: `http://localhost:15672`
- Username: `guest`
- Password: `guest`

### Send a Test Email Message

Using curl:
```bash
curl -X POST http://localhost:8080/api/v1/email/send \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "from": "sender@example.com",
    "subject": "Test Email",
    "body": "This is a test email message"
  }'
```

### Health Check
```bash
curl http://localhost:8080/api/v1/email/health
```

## Message Format

The email message should be in JSON format:

```json
{
  "to": "recipient@example.com",
  "from": "sender@example.com",
  "subject": "Email Subject",
  "body": "Email body content"
}
```

## Consumer Configuration

The consumer is configured with the following features:

- **Concurrency**: 3-10 concurrent consumers
- **Prefetch**: 10 messages per consumer
- **Retry Mechanism**: 
  - Enabled with 3 max attempts
  - Initial interval: 3 seconds
  - Multiplier: 2 (exponential backoff)

## Development

### Building the Project
```bash
mvn clean install
```

### Running Tests
```bash
mvn test
```

### Building Docker Image
```bash
docker build -t email-consumer:latest .
```

## Logging

Logging is configured in `application.properties`:
- Root level: INFO
- Application level: DEBUG
- Spring AMQP level: DEBUG

Logs will show:
- Received messages
- Processing status
- Any errors during processing

## Customization

To customize the email processing logic, edit the `processEmail` method in `EmailConsumer.java`:

```java
private void processEmail(EmailMessage emailMessage) {
    // Add your custom email processing logic here
}
```

## Troubleshooting

### Connection Issues
- Ensure RabbitMQ is running and accessible
- Check the connection parameters in `application.properties`
- Verify network connectivity

### Messages Not Being Consumed
- Check RabbitMQ Management UI to see if messages are in the queue
- Verify queue name matches configuration
- Check application logs for errors

## License

This project is licensed under the MIT License.
