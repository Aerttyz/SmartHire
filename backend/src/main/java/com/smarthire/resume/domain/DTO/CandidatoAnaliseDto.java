package com.smarthire.resume.domain.DTO;

import java.util.List;
import java.util.UUID;

public record CandidatoAnaliseDto(
    UUID id,
    List<String> habilidades,
    List<String> idiomas,
    List<String> formacaoAcademica,
    String experiencia) {
}
