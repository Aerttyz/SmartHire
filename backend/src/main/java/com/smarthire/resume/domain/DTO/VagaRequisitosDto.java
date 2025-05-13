package com.smarthire.resume.domain.DTO;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VagaRequisitosDto {
    private String habilidades;
    private String idiomas;
    private String formacaoAcademica;
    private String experiencia;

    private Double pesoHabilidades;
    private Double pesoIdiomas;
    private Double pesoFormacaoAcademica;
    private Double pesoExperiencia;
}
