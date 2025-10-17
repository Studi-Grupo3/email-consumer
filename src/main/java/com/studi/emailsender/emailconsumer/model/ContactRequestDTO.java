package com.studi.emailsender.emailconsumer.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome pode ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 150, message = "E-mail pode ter no máximo 150 caracteres")
        String email,

        @Size(max = 20, message = "Celular pode ter no máximo 20 caracteres")
        String celular,

        @NotBlank(message = "Mensagem é obrigatória")
        @Size(max = 2000, message = "Mensagem pode ter no máximo 2000 caracteres")
        String mensagem
) {}
