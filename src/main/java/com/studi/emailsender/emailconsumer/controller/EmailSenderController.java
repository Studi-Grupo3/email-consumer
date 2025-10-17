package com.studi.emailsender.emailconsumer.controller;

import com.studi.emailsender.emailconsumer.model.ContactRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/contact")
public class EmailSenderController {

    private static final Logger log = LoggerFactory.getLogger(EmailSenderController.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.email}")
    private String exchange;

    @Value("${rabbitmq.routing.key.email}")
    private String routingKey;

    public EmailSenderController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> enviarContato(@Valid @RequestBody ContactRequestDTO dto) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, dto);
            log.info("Mensagem publicada na fila: {}", dto);
            return ResponseEntity.ok(Map.of("message", "Sua mensagem foi enviada para processamento."));
        } catch (Exception e) {
            log.error("Falha ao publicar mensagem de contato", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Falha ao publicar mensagem de contato."));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}