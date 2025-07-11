package com.smarthire.resume.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smarthire.resume.domain.DTO.CandidatoDto;
import com.smarthire.resume.domain.DTO.CandidatoRequestDTO;
import com.smarthire.resume.domain.DTO.EmailDTO;
import com.smarthire.resume.domain.DTO.VagaResumoDto;
import com.smarthire.resume.domain.model.Candidato;
import com.smarthire.resume.domain.model.Curriculo;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.CandidatoRepository;
import com.smarthire.resume.domain.repository.CurriculoRepository;
import com.smarthire.resume.domain.repository.EmpresaRepositoryJpa;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.exception.ItemNotFoundException;
import com.smarthirepro.core.security.AuthUtils;
import com.smarthirepro.domain.model.Empresa;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CandidatoService {
    private CandidatoRepository candidatoRepository;

    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private CurriculoRepository curriculoRepository;
    @Autowired
    private VagaService vagaService;
    @Autowired
    private EmpresaRepositoryJpa empresaRepository;

    @Transactional
    public Candidato salvar(Candidato candidato) {
        verificarEmailEmUso(candidato.getEmail(), candidato);
        return candidatoRepository.save(candidato);
    }

    public CandidatoDto listarCandidatos(Candidato candidato) {
        Vaga vaga = candidato.getVaga();
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

    public List<CandidatoDto> buscarCandidatoPorNome(String nomeCandidato) {
        List<Candidato> candidatos = candidatoRepository.findByNomeContainingIgnoreCase(nomeCandidato);
        List<CandidatoDto> candidatosDto = candidatos.stream()
                .map(this::listarCandidatos)
                .collect(Collectors.toList());
        if (candidatosDto.isEmpty()) {
            throw new BusinessRuleException("Candidato não encontrado.");
        }
        return candidatosDto;
    }

    public Candidato atualizarCandidatoPorId(UUID id, CandidatoRequestDTO data) {
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Candidato", id));

        Curriculo curriculo = curriculoRepository.findById(data.curriculoId())
                .orElseThrow(() -> new BusinessRuleException("Currículo não encontrado"));

        Vaga vaga = vagaRepository.findById(data.vagaId())
                .orElseThrow(() -> new BusinessRuleException("Vaga não encontrada"));

        verificarNulidadeCurriculo(curriculo);
        verificarVagaAtiva(vaga);
        candidato.atualizarCom(data, curriculo, vaga);

        return candidatoRepository.save(candidato);
    }

    public Candidato atualizarEmailPorId(UUID id, EmailDTO email) {
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Candidato", id));
        candidato.updateEmail(email.email());
        return candidatoRepository.save(candidato);
    }

    @Transactional
    public void deletarCandidatoPorId(UUID id) {
        if (!candidatoRepository.existsById(id)) {
            throw new BusinessRuleException("Candidato não encontrado.");
        }
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Candidato não encontrado."));
        if (candidato.getVaga() != null) {
            throw new BusinessRuleException("Candidato não pode ser deletado, pois está vinculado a uma vaga.");
        }
        candidatoRepository.delete(candidato);
    }

    @Transactional
    public void criarComCurriculo(Curriculo curriculo, UUID idVaga) {
        Candidato candidato = new Candidato();
        verificarNulidadeCurriculo(curriculo);
        candidato.setCurriculo(curriculo);
        candidato.setNome(curriculo.getNome());
        candidato.setEmail(curriculo.getEmail());
        candidato.setTelefone(curriculo.getTelefone());

        Vaga vaga = vagaService.listarPorId(idVaga);
        candidato.setVaga(vaga);
        salvar(candidato);
    }

    public void adicionarCandidatoAVaga(UUID idCandidato, UUID idVaga) {
        if (!candidatoRepository.existsById(idCandidato) || !vagaRepository.existsById(idVaga)) {
            throw new BusinessRuleException("Candidato ou vaga não encontrado.");
        }
        Candidato candidato = candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new BusinessRuleException("Candidato não encontrado."));
        Vaga vaga = vagaRepository.findById(idVaga)
                .orElseThrow(() -> new BusinessRuleException("Vaga não encontrada."));
        if (candidato.getVaga() != null) {
            throw new BusinessRuleException("Candidato já está vinculado a uma vaga.");
        }
        verificarVagaAtiva(vaga);
        candidato.setVaga(vaga);
        candidatoRepository.save(candidato);
    }

    public double contarCandidatosEmpresaLogada() {
        List<UUID> vagaIds = vagaRepository.findVagaIdsByEmpresaId(AuthUtils.getEmpresaId());
        if (vagaIds.isEmpty()) {
            return 0;
        }
        return candidatoRepository.countByVagaIdIn(vagaIds);
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

    private void verificarNulidadeCurriculo(Curriculo curriculo) {
        if (curriculo == null) {
            throw new BusinessRuleException("Currículo não pode ser nulo.");
        }
    }

    private void verificarEmailEmUso(String email, Candidato candidato) {
        boolean emailEmUso = candidatoRepository.findByEmail(candidato.getEmail())
                .filter(e -> !e.equals(candidato))
                .isPresent();
        if (emailEmUso) {
            throw new BusinessRuleException("E-mail já cadastrado no sistema.");
        }
    }

    private void verificarVagaAtiva(Vaga vaga) {
        if (!vaga.isActive()) {
            throw new BusinessRuleException("Essa vaga não está aberta a candidaturas.");
        }
    }
}
