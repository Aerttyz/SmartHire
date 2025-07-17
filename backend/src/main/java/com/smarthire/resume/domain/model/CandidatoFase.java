package com.smarthire.resume.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.smarthirepro.domain.model.Candidato;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "candidato_fase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidatoFase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @OneToOne
    @JoinColumn(name = "candidato_id")
    private Candidato candidato;

    @ManyToOne
    @JoinColumn(name = "fase_id")
    private Fase fase;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;
}