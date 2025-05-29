package com.smarthire.resume.domain.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Data
public class CurriculoApiDto {
  private UUID id;
  private String experiencia;
  private List<String> formacaoAcademica;
  private List<String> habilidades;
  private List<String> idiomas;
}
