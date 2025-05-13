package com.smarthire.resume.domain.DTO;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VagaDto {
    private String nome;
    private UUID empresaId;
    private boolean isActive;

    private String habilidades;
    private String idiomas;
    private String formacaoAcademica;
    private String experiencia;

    private Double pesoHabilidades;
    private Double pesoIdiomas;
    private Double pesoFormacaoAcademica;
    private Double pesoExperiencia;
}
