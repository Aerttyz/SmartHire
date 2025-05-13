package com.smarthire.resume.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vaga_requisitos")
public class VagaRequisitosModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;

    @OneToOne
    @JoinColumn(name = "vaga_id")
    private Vaga vaga;

    private String habilidades;
    private String idiomas;
    private String formacaoAcademica;
    private String experiencia;

    private Double pesoHabilidades;
    private Double pesoIdiomas; 
    private Double pesoFormacaoAcademica;
    private Double pesoExperiencia;
}
