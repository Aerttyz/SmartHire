package com.smarthire.resume.domain.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.UUID;

public record CandidatoDto (
        @NotNull UUID id,
        @NotBlank String nome,
        @Email @NotBlank String email,
        @Pattern(regexp = "\\+?\\d{8,15}") String telefone,
        List<String> habilidades,
        List<String> idiomas,
        List<String> formacaoAcademica,
        String experiencia,
        VagaResumoDto vaga
) {}
