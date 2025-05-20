package com.smarthire.resume.domain.model;

import com.smarthire.resume.domain.DTO.VagaDto;
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

    public void vagaDtoMapper(VagaDto data, Empresa empresa) {
        this.empresa = empresa;
        this.nome = data.nome();
        this.isActive = data.isActive();

        if (this.requisitos == null) {
            this.requisitos = new VagaRequisitosModel();
            this.requisitos.setVaga(this);
        }

        this.requisitos.setHabilidades(data.habilidades());
        this.requisitos.setIdiomas(data.idiomas());
        this.requisitos.setFormacaoAcademica(data.formacaoAcademica());
        this.requisitos.setExperiencia(data.experiencia());
        this.requisitos.setPesoHabilidades(data.pesoHabilidades());
        this.requisitos.setPesoIdiomas(data.pesoIdiomas());
        this.requisitos.setPesoFormacaoAcademica(data.pesoFormacaoAcademica());
        this.requisitos.setPesoExperiencia(data.pesoExperiencia());
    }
}