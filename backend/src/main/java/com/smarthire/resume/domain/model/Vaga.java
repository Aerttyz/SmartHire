package com.smarthire.resume.domain.model;

import com.smarthirepro.domain.model.CargoGenerico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vaga")
public class Vaga extends CargoGenerico {

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    @Column(name = "pontuacao_minima")
    private Double pontuacaoMinima;
}