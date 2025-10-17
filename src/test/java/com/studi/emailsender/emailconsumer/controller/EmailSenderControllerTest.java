package com.studi.emailsender.emailconsumer.controller;

import com.studi.emailsender.emailconsumer.model.ContactRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmailSenderController.class) // ajuste para sua classe de controller correta
class EmailSenderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock do bean que falta no contexto
    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void enviarContato_sucesso_retorna200() throws Exception {
        String json = """
                {
                  "nome": "Fernando",
                  "email": "fernando@example.com",
                  "celular": "11999998888",
                  "mensagem": "Olá, teste"
                }
                """;

        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Sua mensagem foi enviada para processamento."));

        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(ContactRequestDTO.class));
    }

    @Test
    void enviarContato_falhaRabbit_retorna500() throws Exception {
        doThrow(new RuntimeException("rabbit down"))
                .when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(ContactRequestDTO.class));

        String json = """
                {
                  "nome": "joao",
                  "email": "joao@example.com",
                  "celular": "11999998888",
                  "mensagem": "Olá, teste"
                }
                """;

        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                // preferível checar existencia do campo de erro se a mensagem exata variar entre controllers
                .andExpect(jsonPath("$.error").exists());

        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(ContactRequestDTO.class));
    }

    @Test
    void enviarContato_dadosInvalidos_retorna400ComErros() throws Exception {
        String invalidJson = """
                {
                  "nome": "",
                  "email": "nao-e-um-email",
                  "mensagem": ""
                }
                """;

        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").value("Nome é obrigatório"))
                .andExpect(jsonPath("$.email").value("E-mail inválido"))
                .andExpect(jsonPath("$.mensagem").value("Mensagem é obrigatória"));

        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any(ContactRequestDTO.class));
    }
}
