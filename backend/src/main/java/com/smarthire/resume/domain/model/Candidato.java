package com.smarthire.resume.domain.model;

import com.smarthire.resume.domain.enums.Situacao;
import jakarta.persistence.*;
import lombok.*;

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

    private String name;

    private String email;

    private String telefone;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;
}
