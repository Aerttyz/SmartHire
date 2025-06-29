package com.smarthire.resume.domain.model;

import java.util.List;
import java.util.UUID;

import com.smarthirepro.domain.model.Empresa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private boolean isActive;

    @OneToOne(mappedBy = "vaga", cascade = CascadeType.ALL)
    private VagaRequisitosModel requisitos;

    @OneToMany(mappedBy = "vaga", cascade = CascadeType.ALL)
    private List<Candidato> candidatos;

    @OneToMany(mappedBy = "vaga", cascade = CascadeType.ALL)
    @OrderBy("ordem ASC")
    private List<Fase> fases;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    @Column(name = "pontuacao_minima")
    private Double pontuacaoMinima;

    // public void vagaDtoMapper(VagaPatchResposta data, Empresa empresa) {
    // if (empresa != null) {
    // this.empresa = empresa;
    // }

    // if (data.nome() != null && !data.nome().isBlank()) {
    // this.nome = data.nome();
    // }

    // this.isActive = data.isActive();

    // if (this.requisitos == null) {
    // this.requisitos = new VagaRequisitosModel();
    // this.requisitos.setVaga(this);
    // }

    // if (data.habilidades() != null && !data.habilidades().isBlank()) {
    // this.requisitos.setHabilidades(data.habilidades());
    // }

    // if (data.idiomas() != null && !data.idiomas().isBlank()) {
    // this.requisitos.setIdiomas(data.idiomas());
    // }

    // if (data.formacaoAcademica() != null && !data.formacaoAcademica().isBlank())
    // {
    // this.requisitos.setFormacaoAcademica(data.formacaoAcademica());
    // }

    // if (data.experiencia() != null && !data.experiencia().isBlank()) {
    // this.requisitos.setExperiencia(data.experiencia());
    // }

    // if (data.pesoHabilidades() != null) {
    // this.requisitos.setPesoHabilidades(data.pesoHabilidades());
    // }

    // if (data.pesoIdiomas() != null) {
    // this.requisitos.setPesoIdiomas(data.pesoIdiomas());
    // }

    // if (data.pesoFormacaoAcademica() != null) {
    // this.requisitos.setPesoFormacaoAcademica(data.pesoFormacaoAcademica());
    // }

    // if (data.pesoExperiencia() != null) {
    // this.requisitos.setPesoExperiencia(data.pesoExperiencia());
    // }

    // if (data.pontuacaoMinima() != null) {
    // this.pontuacaoMinima = data.pontuacaoMinima();
    // }
    // }
}