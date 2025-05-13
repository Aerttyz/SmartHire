package com.smarthire.resume.domain.DTO;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoDto {
    private UUID id;
    private String nome;
    private String email;
    private String telefone;
    private List<String> habilidades;
    private List<String> idiomas;
    private List<String> formacaoAcademica;
    private String experiencia;
    private VagaResumoDto vaga;
}
