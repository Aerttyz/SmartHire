package com.smarthire.resume.domain.model;

import com.smarthire.resume.domain.DTO.VagaRequestDTO;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vaga")
public class Vaga {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    private String nome;

    private String descricao;
    private boolean isActive;

    @OneToOne(mappedBy = "vaga", cascade = CascadeType.ALL)
    private VagaRequisitosModel requisitos;

    @OneToMany(mappedBy = "vaga", cascade = CascadeType.ALL)
    private List<Candidato> candidatos;

    public void atualizarCom(VagaRequestDTO data, Empresa empresa) {
        this.empresa = empresa;
        this.nome = data.nome();
        this.descricao = data.descricao();
        this.isActive = data.isActive();
    }
}