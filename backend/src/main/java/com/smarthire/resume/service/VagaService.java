package com.smarthire.resume.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smarthire.resume.domain.DTO.VagaDto;
import com.smarthire.resume.domain.DTO.VagaRequisitosDto;
import com.smarthire.resume.domain.DTO.VagaRespostaDto;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.model.VagaRequisitosModel;
import com.smarthire.resume.domain.repository.EmpresaRepositoryJpa;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthirepro.core.exception.BusinessRuleException;
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

    @Transactional
    public void salvar(VagaDto dto) {
        validarPesos(dto);
        UUID id = AuthUtils.getEmpresaId();
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Empresa com Id" + dto.empresaId() + " não encontrada."));
        Vaga vaga = new Vaga();
        vaga.setNome(dto.nome());
        vaga.setEmpresa(empresa);
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

        requisitos.setCargo(vaga);
        vaga.setRequisitos(requisitos);

        vagaRepository.save(vaga);
    }

    private VagaRespostaDto listar(Vaga vaga) {
        VagaRequisitosDto requisitosDto = null;

        if (vaga.getRequisitos() != null) {
            VagaRequisitosModel requisitos = (VagaRequisitosModel) vaga.getRequisitos();

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

        return new VagaRespostaDto(
                vaga.getId(),
                vaga.getNome(),
                vaga.isActive(),
                vaga.getEmpresa().getNome(),
                requisitosDto);
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