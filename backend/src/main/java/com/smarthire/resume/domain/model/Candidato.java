package com.smarthire.resume.domain.model;

import com.smarthire.resume.domain.enums.Situacao;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidato")
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    private String nome;

    private String email;

    private String telefone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "curriculo_id")
    private Curriculo curriculo;

    @OneToOne
    private Vaga vaga;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;
}
