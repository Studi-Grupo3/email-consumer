package com.studi.emailsender.emailconsumer.controller;

import com.studi.emailsender.emailconsumer.model.ContactRequestDTO;
import com.studi.emailsender.emailconsumer.service.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ContactRequestListener {

    private static final Logger log = LoggerFactory.getLogger(ContactRequestListener.class);

    private final EmailSenderService emailSenderService;

    public ContactRequestListener(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @RabbitListener(queues = "email.contact.queue")
    public void receiveContactRequest(ContactRequestDTO dto) {
        log.info("Listener recebido contato: {}", dto);
        try {
            emailSenderService.sendContactEmail(dto);
            log.info("Processamento do contato completo: {}", dto);
        } catch (Exception e) {
            log.error("Erro ao processar contato: {}", dto, e);
        }
    }
}
