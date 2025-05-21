package com.smarthire.resume.domain.DTO;

import java.util.UUID;
import java.util.List;

public record VagaRespostaDto (
    UUID id,
    String nome,
    boolean isActive,
    String empresaNome,
    VagaRequisitosDto requisitos,
    List<FaseDto> fase
){}
