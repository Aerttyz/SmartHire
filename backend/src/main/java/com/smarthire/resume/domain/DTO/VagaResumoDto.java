package com.smarthire.resume.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public record VagaResumoDto (
    @NotNull UUID id,
    @NotBlank String nome
){}
