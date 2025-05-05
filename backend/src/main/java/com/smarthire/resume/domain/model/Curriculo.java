package com.smarthire.resume.domain.model;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import jakarta.persistence.*;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Curriculo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    private String nome;

    private String email;

    private String telefone;
    
    private String experiencia;

    @ElementCollection
    private List<String> formacaoAcademica = new ArrayList<>();

    @ElementCollection
    private List<String> habilidades = new ArrayList<>();

    @ElementCollection
    private List<String> idiomas = new ArrayList<>();
}
