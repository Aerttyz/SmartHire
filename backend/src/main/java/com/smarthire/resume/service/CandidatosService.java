package com.smarthire.resume.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smarthire.resume.domain.DTO.CandidatoDto;
import com.smarthire.resume.domain.DTO.VagaResumoDto;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.CandidatoRepositoryJpa;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthirepro.core.exception.BusinessRuleException;
import com.smarthirepro.core.security.AuthUtils;
import com.smarthirepro.domain.model.Candidato;
import com.smarthirepro.domain.model.Curriculo;
import com.smarthirepro.domain.model.Empresa;
import com.smarthirepro.domain.repositories.EmpresaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CandidatosService {

    private final VagaRepository vagaRepository;
    private final CandidatoRepositoryJpa candidatoRepository;
    private final EmpresaRepository empresaRepository;

    public double contarCandidatosEmpresaLogada() {
        List<UUID> vagaIds = vagaRepository.findVagaIdsByEmpresaId(AuthUtils.getEmpresaId());
        if (vagaIds.isEmpty()) {
            return 0;
        }
        return candidatoRepository.countByCargoIdIn(vagaIds);
    }

    public double obterMediaCandidatoVaga() {
        UUID id = AuthUtils.getEmpresaId();
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Empresa não encontrada."));

        double numeroVagas = vagaRepository.countByEmpresaId(empresa.getId());
        if (numeroVagas == 0) {
            return 0;
        }

        double candidatos = contarCandidatosEmpresaLogada();
        return candidatos / numeroVagas;
    }

    public CandidatoDto listarCandidatos(Candidato candidato) {
        Vaga vaga = (Vaga) candidato.getCargo();
        Curriculo curriculo = candidato.getCurriculo();
        verificarNulidadeCurriculo(curriculo);

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
                vagaResumoDto);
    }

    public List<CandidatoDto> listarTodos() {
        List<Candidato> candidatos = candidatoRepository.findAll();
        return candidatos.stream()
                .map(c -> {
                    try {
                        return listarCandidatos(c);
                    } catch (BusinessRuleException e) {
                        throw new BusinessRuleException("Erro ao listar candidatos: " + e.getMessage());
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<CandidatoDto> listarTodosPorEmpresaId() {
        UUID empresaId = AuthUtils.getEmpresaId();
        List<Candidato> candidatos = candidatoRepository.findByEmpresaId(empresaId);
        return candidatos.stream()
                .map(c -> {
                    try {
                        return listarCandidatos(c);
                    } catch (BusinessRuleException e) {
                        throw new BusinessRuleException("Erro ao listar candidatos da empresa: " + e.getMessage());
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void verificarNulidadeCurriculo(Curriculo curriculo) {
        if (curriculo == null) {
            throw new BusinessRuleException("Currículo não pode ser nulo.");
        }
    }
}
