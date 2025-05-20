package com.smarthire.resume.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VagaRequestDTO(
        @NotNull UUID empresaId,
        @NotBlank String nome,
        @NotBlank String descricao,
        @NotNull Boolean isActive
) {
}
