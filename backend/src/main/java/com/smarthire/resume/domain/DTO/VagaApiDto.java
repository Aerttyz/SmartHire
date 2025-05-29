// Exemplo: VagaApiDTO.java
package com.smarthire.resume.domain.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Data
public class VagaApiDto {
  private UUID id;

  private String nome;
  private String requisitosHabilidades;
  private String requisitosIdiomas;
  private String requisitosFormacaoAcademica;
  private String requisitosExperiencia;

  private Double pesoHabilidades;
  private Double pesoIdiomas;
  private Double pesoFormacaoAcademica;
  private Double pesoExperiencia;
}