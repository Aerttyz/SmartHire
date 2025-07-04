package com.smarthire.resume.domain.DTO;

import java.util.UUID;

public record VagaRespostaDto(
        UUID id,
        String nome,
        boolean isActive,
        String empresaNome,
        VagaRequisitosDto requisitos) {
}
