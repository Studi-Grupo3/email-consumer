package com.studi.emailsender.emailconsumer.service;

import com.studi.emailsender.emailconsumer.model.ContactRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailSenderServiceTest {

    private JavaMailSender mailSender;
    private EmailSenderService service;

    @BeforeEach
    void setUp() {
        mailSender = Mockito.mock(JavaMailSender.class);
        service = new EmailSenderService(mailSender);
        ReflectionTestUtils.setField(service, "contatoDestino", "destino@example.com");
        ReflectionTestUtils.setField(service, "remetentePadrao", "noreply@example.com");
    }

    @Test
    void sendContactEmail_semDestinoConfigurado_lancaIllegalStateException() {
        ReflectionTestUtils.setField(service, "contatoDestino", "");
        ContactRequestDTO dto = new ContactRequestDTO("Nome", "email@x.com", "1199999", "Mensagem");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.sendContactEmail(dto));
        assertTrue(ex.getMessage().contains("Destino de contato não configurado"));
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendContactEmail_comDestino_configuraEMandaEmail() {
        ContactRequestDTO dto = new ContactRequestDTO("Maria", "maria@example.com", "11988887777", "Olá, teste de mensagem");

        service.sendContactEmail(dto);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage sent = captor.getValue();
        assertArrayEquals(new String[]{"destino@example.com"}, sent.getTo());
        assertEquals("noreply@example.com", sent.getFrom());
        assertTrue(sent.getSubject().contains("Novo contato de Maria"));
        assertTrue(sent.getText().contains("Nome: Maria"));
        assertTrue(sent.getText().contains("E-mail: maria@example.com"));
        assertTrue(sent.getText().contains("Mensagem:\nOlá, teste de mensagem"));
    }
}
