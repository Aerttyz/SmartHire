package com.smarthire.resume.domain.DTO;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VagaRespostaDto {
    private UUID id;
    private String nome;
    private boolean isActive;
    private String empresaNome;
    private VagaRequisitosDto requisitos;
}
