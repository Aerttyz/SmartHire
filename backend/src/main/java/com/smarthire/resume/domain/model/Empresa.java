package com.smarthire.resume.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;


    @EqualsAndHashCode.Include
    private String cnpj;

    private String nome;
    private String email;
    private String senha;
    private String telefone;
}
