package com.smarthire.resume.service;

import com.smarthire.resume.domain.DTO.CandidatoDto;
import com.smarthire.resume.domain.DTO.VagaRespostaDto;
import com.smarthire.resume.domain.DTO.VagaResumoDto;
import com.smarthire.resume.domain.enums.Situacao;
import com.smarthire.resume.domain.model.Candidato;
import com.smarthire.resume.domain.model.Curriculo;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.CandidatoRepository;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CandidatoService {
    private CandidatoRepository candidatoRepository;

    @Autowired
    private VagaRepository vagaRepository;

    @Transactional
    public Candidato salvar(Candidato candidato) {
        boolean emailEmUso = candidatoRepository.findByEmail(candidato.getEmail())
                .filter(e-> !e.equals(candidato))
                .isPresent();
        if (emailEmUso) {
            throw new BusinessRuleException("E-mail já cadastrado no sistema.");
        }
        return candidatoRepository.save(candidato);
    }

    public CandidatoDto listarCandidatos(Candidato candidato) {
        Vaga vaga = candidato.getVaga();
        Curriculo curriculo = candidato.getCurriculo();
        if(curriculo == null) {
            throw new BusinessRuleException("Candidato não possui currículo");
        }

        VagaResumoDto vagaResumoDto = null;
        if (vaga != null) {
            vagaResumoDto = new VagaResumoDto(vaga.getId(), vaga.getNome());
        }

        return new CandidatoDto(
                candidato.getId(),
                candidato.getNome(),
                candidato.getEmail(),
                candidato.getTelefone(),
                curriculo.getHabilidades(),
                curriculo.getIdiomas(),
                curriculo.getFormacaoAcademica(),
                curriculo.getExperiencia(),
                vagaResumoDto
        );
    }

    public Candidato buscarCandidatoPorNome(String nomeCandidato) {
        return candidatoRepository.findByNome(nomeCandidato)
                .orElseThrow(() -> new BusinessRuleException("Candidato não encontrado."));
    }

    public void deletarCandidatoPorId(UUID id){
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Candidato não encontrado."));
        candidatoRepository.delete(candidato);
    }

    public void criarComCurriculo(Curriculo curriculo) {
        Candidato candidato = new Candidato();
        candidato.setCurriculo(curriculo);
        candidato.setNome(curriculo.getNome());
        candidato.setEmail(curriculo.getEmail());
        candidato.setTelefone(curriculo.getTelefone());
        candidato.setSituacao(Situacao.TRIAGEM);

        salvar(candidato);
    }

    public void adicionarCandidatoAVaga(UUID idCandidato, UUID idVaga) {
        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new BusinessRuleException("Candidato não encontrado."));
        Vaga vaga = vagaRepository.findById(idVaga)
                .orElseThrow(() -> new BusinessRuleException("Vaga não encontrada."));
        if(candidato.getVaga() != null) {
            throw new BusinessRuleException("Candidato já está vinculado a uma vaga.");
        }
        if(!vaga.isActive()) {
            throw new BusinessRuleException("Essa vaga não está aberta a candidaturas");
        }
        candidato.setVaga(vaga);
        candidatoRepository.save(candidato);
    }
}
