package com.smarthire.resume.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FaseDto(
    
    @NotBlank(message = "O titulo da fase é obrigatório")
    String titulo,

    String descricao,

    @NotNull(message = "A ordem da fase é obrigatória")
    Integer ordem
) {}
