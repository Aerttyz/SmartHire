package com.smarthire.resume.domain.DTO;

import java.util.List;
import java.util.UUID;

public record CurriculoApiDto(
   UUID id,
   String experiencia,
   List<String> formacaoAcademica,
   List<String> habilidades,
   List<String> idiomas) {}
