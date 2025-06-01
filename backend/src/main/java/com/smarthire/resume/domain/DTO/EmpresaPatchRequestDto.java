package com.smarthire.resume.domain.DTO;

public record EmpresaPatchRequestDto(
    String nome,
    String cnpj,
    String telefone,
    String email,
    String senha
) {}