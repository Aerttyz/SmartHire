package com.smarthire.resume.domain.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDTO  (
        @Email(message = "Email inválido")
        @NotBlank(message = "Email não pode ser vazio")
        String email
){}
