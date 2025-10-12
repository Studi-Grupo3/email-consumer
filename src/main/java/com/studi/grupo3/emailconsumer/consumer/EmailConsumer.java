package com.studi.grupo3.emailconsumer.consumer;

import com.studi.grupo3.emailconsumer.model.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Email Consumer - Listens to RabbitMQ queue for email messages
 */
@Slf4j
@Component
public class EmailConsumer {

    /**
     * Consumes email messages from the queue
     * 
     * @param emailMessage the email message received from the queue
     */
    @RabbitListener(queues = "${rabbitmq.queue.email}")
    public void consumeEmailMessage(EmailMessage emailMessage) {
        log.info("Received email message: {}", emailMessage);
        
        try {
            // Process the email message
            processEmail(emailMessage);
            log.info("Successfully processed email to: {}", emailMessage.getTo());
        } catch (Exception e) {
            log.error("Error processing email message: {}", emailMessage, e);
            throw e; // Re-throw to trigger retry mechanism
        }
    }

    /**
     * Process the email message
     * This is where you would implement your email sending logic
     * 
     * @param emailMessage the email message to process
     */
    private void processEmail(EmailMessage emailMessage) {
        // TODO: Implement your email sending logic here
        // For now, we just log the message
        log.debug("Processing email:");
        log.debug("From: {}", emailMessage.getFrom());
        log.debug("To: {}", emailMessage.getTo());
        log.debug("Subject: {}", emailMessage.getSubject());
        log.debug("Body: {}", emailMessage.getBody());
        
        // Simulate some processing time
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Processing interrupted", e);
        }
    }
}
