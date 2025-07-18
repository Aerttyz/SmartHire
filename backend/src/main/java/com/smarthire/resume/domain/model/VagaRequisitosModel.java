package com.smarthire.resume.domain.model;

import com.smarthirepro.domain.model.CargoCompetenciasGenerico;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vaga_requisitos")
public class VagaRequisitosModel extends CargoCompetenciasGenerico {

    private Double pesoHabilidades;
    private Double pesoIdiomas;
    private Double pesoFormacaoAcademica;
    private Double pesoExperiencia;
}
