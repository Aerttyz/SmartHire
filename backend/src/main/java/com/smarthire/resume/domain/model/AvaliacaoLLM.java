package com.smarthire.resume.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvaliacaoLLM {
  private String compatibilidade;
  private List<String> pontosFortes;
  private List<String> lacunasIdentificadas;
  private String sugestoesParaEmpresa;
}
