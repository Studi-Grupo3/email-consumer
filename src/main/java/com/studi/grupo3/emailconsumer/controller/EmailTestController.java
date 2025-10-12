package com.studi.grupo3.emailconsumer.controller;

import com.studi.grupo3.emailconsumer.model.EmailMessage;
import com.studi.grupo3.emailconsumer.service.EmailProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Test Controller - For testing email message publishing
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailProducerService emailProducerService;

    /**
     * Send a test email message to the queue
     * 
     * @param emailMessage the email message to send
     * @return response entity with status
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailMessage emailMessage) {
        log.info("Received request to send email: {}", emailMessage);
        
        try {
            emailProducerService.sendEmailMessage(emailMessage);
            return ResponseEntity.ok("Email message sent to queue successfully");
        } catch (Exception e) {
            log.error("Error sending email message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending email message: " + e.getMessage());
        }
    }

    /**
     * Health check endpoint
     * 
     * @return health status
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Email Consumer is running");
    }
}
