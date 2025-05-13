package com.smarthire.resume.domain.DTO;

import lombok.*;
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
    private VagaResumoDto vaga;
}
