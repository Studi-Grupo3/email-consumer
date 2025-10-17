package com.studi.emailsender.emailconsumer.service;

import com.studi.emailsender.emailconsumer.model.ContactRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private static final Logger log = LoggerFactory.getLogger(EmailSenderService.class);

    private final JavaMailSender mailSender;

    @Value("${contato.email.destino:}")
    private String contatoDestino;

    @Value("${contato.email.remetente:noreply@example.com}")
    private String remetentePadrao;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendContactEmail(ContactRequestDTO dto) {
        if (contatoDestino == null || contatoDestino.isBlank()) {
            throw new IllegalStateException("Destino de contato não configurado (contato.email.destino)");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(remetentePadrao);
        message.setTo(contatoDestino);
        message.setSubject("Novo contato de " + dto.nome());

        StringBuilder corpo = new StringBuilder();
        corpo.append("Você recebeu uma nova mensagem de contato no site.\n\n");
        corpo.append("Nome: ").append(dto.nome()).append("\n");
        corpo.append("E-mail: ").append(dto.email()).append("\n");
        if (dto.celular() != null && !dto.celular().isBlank()) {
            corpo.append("Celular: ").append(dto.celular()).append("\n");
        }
        corpo.append("\nMensagem:\n").append(dto.mensagem()).append("\n");
        corpo.append("\n--\nEnviado automaticamente.");

        message.setText(corpo.toString());

        log.info("Enviando e-mail de contato: {}", message);
        mailSender.send(message);
        log.info("E-mail enviado com sucesso");
    }
}