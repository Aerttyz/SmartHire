package com.smarthire.resume.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.smarthire.resume.domain.DTO.FaseDto;
import com.smarthire.resume.domain.DTO.VagaDto;
import com.smarthire.resume.domain.DTO.VagaRequisitosDto;
import com.smarthire.resume.domain.DTO.VagaRespostaDto;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.model.VagaRequisitosModel;
import com.smarthire.resume.domain.repository.EmpresaRepositoryJpa;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.exception.InvalidScoreWeightsException;
import com.smarthire.resume.exception.ItemNotFoundException;
import com.smarthirepro.core.security.AuthUtils;
import com.smarthirepro.domain.model.Empresa;

import jakarta.transaction.Transactional;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private EmpresaRepositoryJpa empresaRepository;
    @Autowired
    private EmpresaService empresaService;

    @Transactional
    public void salvar(VagaDto dto) {
        validarPesos(dto);
        Empresa empresaLogada = empresaService.getEmpresaAutenticada();
        UUID id = empresaLogada.getId();
        Vaga vaga = new Vaga();
        vaga.setNome(dto.nome());
        vaga.setEmpresaId(id);
        vaga.setActive(dto.isActive());
        vaga.setPontuacaoMinima(dto.pontuacaoMinima());

        VagaRequisitosModel requisitos = new VagaRequisitosModel();
        requisitos.setHabilidades(dto.habilidades());
        requisitos.setExperiencia(dto.experiencia());
        requisitos.setFormacaoAcademica(dto.formacaoAcademica());
        requisitos.setIdiomas(dto.idiomas());
        requisitos.setPesoHabilidades(dto.pesoHabilidades());
        requisitos.setPesoIdiomas(dto.pesoIdiomas());
        requisitos.setPesoFormacaoAcademica(dto.pesoFormacaoAcademica());
        requisitos.setPesoExperiencia(dto.pesoExperiencia());

        requisitos.setVaga(vaga);
        vaga.setRequisitos(requisitos);

        vagaRepository.save(vaga);
    }

    private VagaRespostaDto listar(Vaga vaga) {
        Empresa empresa = empresaService.findById(vaga.getEmpresaId())
                .orElseThrow(() -> new BusinessRuleException("Dados da empresa não encontrados."));
        VagaRequisitosDto requisitosDto = null;

        if (vaga.getRequisitos() != null) {
            VagaRequisitosModel requisitos = vaga.getRequisitos();

            requisitosDto = new VagaRequisitosDto(
                    requisitos.getHabilidades(),
                    requisitos.getIdiomas(),
                    requisitos.getFormacaoAcademica(),
                    requisitos.getExperiencia(),
                    requisitos.getPesoHabilidades(),
                    requisitos.getPesoIdiomas(),
                    requisitos.getPesoFormacaoAcademica(),
                    requisitos.getPesoExperiencia());
        }

        List<FaseDto> fases = Collections.emptyList();
        if (vaga.getFases() != null) {
            fases = vaga.getFases().stream()
                    .map(fase -> new FaseDto(
                            fase.getTitulo(),
                            fase.getDescricao(),
                            fase.getOrdem()))
                    .collect(Collectors.toList());
        }
        return new VagaRespostaDto(
                vaga.getId(),
                vaga.getNome(),
                vaga.isActive(),
                empresa.getNome(),
                requisitosDto,
                fases
        );
    }

    public List<VagaRespostaDto> listarPorNome(String nomeVaga) {
        List<Vaga> vagaOptional = vagaRepository.findByNome(nomeVaga);
        List<VagaRespostaDto> vagaRespostaDto = vagaOptional.stream()
                .map(this::listar)
                .collect(Collectors.toList());
        if (vagaRespostaDto.isEmpty()) {
            throw new BusinessRuleException("Vaga não encontrada.");
        }
        return vagaRespostaDto;
    }

    public VagaRespostaDto listarVagaPorId(UUID id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Vaga", id));
        return listar(vaga);
    }

    public List<VagaRespostaDto> listarTodasPorEmpresa() {
        List<Vaga> vagas = vagaRepository.findByEmpresaId(AuthUtils.getEmpresaId());
        return vagas.stream()
                .map(this::listar)
                .collect(Collectors.toList());
    }

    // public VagaRespostaDto atualizarVagaPorId(UUID id, VagaPatchResposta data) {
    // Vaga vaga = vagaRepository.findById(id)
    // .orElseThrow(() -> new ItemNotFoundException("Vaga", id));
    // Empresa empresa = empresaRepository.findById(AuthUtils.getEmpresaId())
    // .orElseThrow(() -> new BusinessRuleException("Empresa não encontrada"));
    // vaga.vagaDtoMapper(data, empresa);
    // Vaga vagaAtualizada = vagaRepository.save(vaga);
    // return listar(vagaAtualizada);
    // }

    public void excluir(UUID id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Vaga não encontrada."));
        vagaRepository.delete(vaga);
    }

    private void validarPesos(VagaDto dto) {
        double somaPesos = dto.pesoHabilidades() +
                dto.pesoIdiomas() +
                dto.pesoFormacaoAcademica() +
                dto.pesoExperiencia();

        if (Math.abs(somaPesos - 1.0) > 0.0001) {
            throw new InvalidScoreWeightsException("A soma dos pesos deve ser igual a 1.0. Valor atual: " + somaPesos);
        }
    }

    public Vaga listarPorId(UUID id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Vaga", id));
        return vaga;
    }

}