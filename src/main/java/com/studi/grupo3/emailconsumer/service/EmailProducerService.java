package com.studi.grupo3.emailconsumer.service;

import com.studi.grupo3.emailconsumer.model.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Email Producer Service - For testing purposes
 * This service can be used to send test messages to the queue
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.email}")
    private String emailExchange;

    @Value("${rabbitmq.routing.key.email}")
    private String emailRoutingKey;

    /**
     * Send email message to RabbitMQ queue
     * 
     * @param emailMessage the email message to send
     */
    public void sendEmailMessage(EmailMessage emailMessage) {
        log.info("Sending email message: {}", emailMessage);
        rabbitTemplate.convertAndSend(emailExchange, emailRoutingKey, emailMessage);
        log.info("Email message sent successfully");
    }
}
