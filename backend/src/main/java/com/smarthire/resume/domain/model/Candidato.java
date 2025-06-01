package com.smarthire.resume.domain.model;
import com.smarthire.resume.domain.DTO.CandidatoRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @ManyToOne
    @JoinColumn(name = "vaga_id")
    private Vaga vaga;

    @OneToOne(mappedBy = "candidato", cascade = CascadeType.ALL)
    private CandidatoFase candidatoFase;

    public void atualizarCom(CandidatoRequestDTO data, Curriculo curriculo, Vaga vaga) {
        this.nome = data.nome();
        this.email = data.email();
        this.telefone = data.telefone();
        this.curriculo = curriculo;
        this.vaga = vaga;
    }

    public void updateEmail(String novoEmail) {
        this.email = novoEmail;
    }

}
