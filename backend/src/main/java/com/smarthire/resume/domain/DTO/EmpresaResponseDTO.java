package com.smarthire.resume.domain.DTO;

import com.smarthirepro.domain.model.Empresa;

// dto criada para isolar o trânsito de senhas no frontend
public record EmpresaResponseDTO(
    String id,
    String nome,
    String cnpj,
    String email) {
  public EmpresaResponseDTO(Empresa empresa) {
    this(empresa.getId().toString(), empresa.getNome(), empresa.getCnpj(), empresa.getEmail());
  }
}
