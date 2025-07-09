package com.smarthire.resume.domain.DTO;

import java.util.List;

import com.smarthirepro.domain.model.Candidato;
import com.smarthirepro.domain.model.Curriculo;

public record CandidatoAnaliseDto(
                String nome,
                String email,
                String experiencia,
                List<String> formacaoAcademica,
                List<String> habilidades,
                List<String> idiomas) {
        public static CandidatoAnaliseDto from(Candidato candidato) {
                Curriculo curriculo = candidato.getCurriculo();
                return new CandidatoAnaliseDto(
                                candidato.getNome(),
                                candidato.getEmail(),
                                curriculo != null ? curriculo.getExperiencia() : null,
                                curriculo != null ? curriculo.getFormacaoAcademica() : List.of(),
                                curriculo != null ? curriculo.getHabilidades() : List.of(),
                                curriculo != null ? curriculo.getIdiomas() : List.of());
        }
}
