package com.studi.grupo3.emailconsumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Configuration
 */
@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.email}")
    private String emailQueue;

    @Value("${rabbitmq.exchange.email}")
    private String emailExchange;

    @Value("${rabbitmq.routing.key.email}")
    private String emailRoutingKey;

    /**
     * Email Queue
     */
    @Bean
    public Queue emailQueue() {
        return new Queue(emailQueue, true);
    }

    /**
     * Email Exchange
     */
    @Bean
    public TopicExchange emailExchange() {
        return new TopicExchange(emailExchange);
    }

    /**
     * Binding between Queue and Exchange using routing key
     */
    @Bean
    public Binding binding(Queue emailQueue, TopicExchange emailExchange) {
        return BindingBuilder
                .bind(emailQueue)
                .to(emailExchange)
                .with(emailRoutingKey);
    }

    /**
     * Message converter to convert Java objects to JSON
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitTemplate for sending messages
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
