package com.smarthire.resume.domain.model;
import java.time.LocalDateTime;

import lombok.*;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

    @ManyToOne
    @JoinColumn(name = "candidato_id")
    private Candidato candidato;

    @ManyToOne
    @JoinColumn(name = "fase_id")
    private Fase fase;

    private LocalDateTime dataInicio;
    
    private LocalDateTime dataFim;
}
