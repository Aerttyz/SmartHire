package com.smarthire.resume.domain.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CandidatoRequestDTO(
        @NotBlank  String nome,
        @NotBlank @Email String email,
        @NotBlank String telefone,
        @NotNull UUID curriculoId,
        @NotNull UUID vagaId,
        @NotBlank String situacao
    ) {
}
