package com.smarthire.resume.domain.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmpresaRequestDTO(
    @NotBlank String nome,
    @NotBlank String cnpj,
    @Email @NotBlank String email,
    @NotBlank String telefone
){}
