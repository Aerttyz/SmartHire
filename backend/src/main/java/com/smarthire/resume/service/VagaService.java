package com.smarthire.resume.service;

import java.util.Collections;

import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.DTO.FaseDto;
import com.smarthire.resume.domain.DTO.VagaDto;
import com.smarthire.resume.domain.DTO.VagaRequisitosDto;
import com.smarthire.resume.domain.DTO.VagaRespostaDto;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.model.VagaRequisitosModel;
import com.smarthire.resume.domain.repository.EmpresaRepository;
import com.smarthire.resume.domain.repository.FaseRepository;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;

import com.smarthire.resume.exception.InvalidScoreWeightsException;
import com.smarthire.resume.exception.ItemNotFoundException;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.smarthire.resume.domain.model.Fase;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private FaseRepository faseRepository;

    //--------------------------- serviços para o front ------------------------------------------
    @Transactional
    public void salvarAutenticado(VagaDto dto, Authentication authentication) {
        validarPesos(dto);
        Empresa empresa = obterEmpresaAutenticada(authentication);

        Vaga vaga = new Vaga();
        vaga.setNome(dto.nome());
        vaga.setEmpresa(empresa);
        vaga.setActive(dto.isActive());

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

    public List<VagaRespostaDto> listarTodasAutenticado(Authentication authentication) {
        Empresa empresa = obterEmpresaAutenticada(authentication);
        List<Vaga> vagas = vagaRepository.findByEmpresa(empresa);
        return vagas.stream().map(this::listar).collect(Collectors.toList());
    }

    public List<VagaRespostaDto> listarPorNomeAutenticado(String nome, Authentication authentication) {
        Empresa empresa = obterEmpresaAutenticada(authentication);
        List<Vaga> vagas = vagaRepository.findByNomeContainingIgnoreCaseAndEmpresa(nome, empresa);
        return vagas.stream().map(this::listar).collect(Collectors.toList());
    }

    public VagaRespostaDto atualizarVagaAutenticado(UUID id, VagaDto dto, Authentication authentication) {
        validarPesos(dto);
        Empresa empresa = obterEmpresaAutenticada(authentication);

        Vaga vaga = vagaRepository.findById(id)
          .orElseThrow(() -> new ItemNotFoundException("Vaga", id));

        if (!vaga.getEmpresa().getId().equals(empresa.getId())) {
            throw new AccessDeniedException("Você não tem permissão para atualizar esta vaga.");
        }

        vaga.vagaDtoMapper(dto, empresa);
        Vaga vagaAtualizada = vagaRepository.save(vaga);
        return listar(vagaAtualizada);
    }

    @Transactional
    public void excluirAutenticado(UUID id, Authentication authentication) {
        Empresa empresa = obterEmpresaAutenticada(authentication);

        Vaga vaga = vagaRepository.findById(id)
          .orElseThrow(() -> new ItemNotFoundException("Vaga", id));

        if (!vaga.getEmpresa().getId().equals(empresa.getId())) {
            throw new AccessDeniedException("Você não tem permissão para excluir esta vaga.");
        }

        vagaRepository.delete(vaga);
    }

    private Empresa obterEmpresaAutenticada(Authentication authentication) {
        String emailEmpresa = authentication.getName();
        return empresaRepository.findByEmail(emailEmpresa)
          .orElseThrow(() -> new UsernameNotFoundException("Empresa autenticada não encontrada."));
    }

    //--------------------------------- fim dos serviços para o front --------------------------------------

    @Transactional
    public void salvar(VagaDto dto) {
        validarPesos(dto);
        Empresa empresa = empresaRepository.findById(dto.empresaId())
                .orElseThrow(() -> new BusinessRuleException("Empresa com Id" + dto.empresaId() + " não encontrada."));
        Vaga vaga = new Vaga();
        vaga.setNome(dto.nome());
        vaga.setEmpresa(empresa);
        vaga.setActive(dto.isActive());

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

    public VagaRespostaDto listar(Vaga vaga) {
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
                    requisitos.getPesoExperiencia()
            );
        }

        List<FaseDto> fases = Collections.emptyList();
        if (vaga.getFases() != null) {
            fases = vaga.getFases().stream()
                    .map(fase -> new FaseDto(
                            fase.getTitulo(),
                            fase.getDescricao(),
                            fase.getOrdem()
                    ))
                    .collect(Collectors.toList());
        }
        return new VagaRespostaDto(
                vaga.getId(),
                vaga.getNome(),
                vaga.isActive(),
                vaga.getEmpresa().getNome(),
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

    public List<VagaRespostaDto> listarTodas() {
        List<Vaga> vagas = vagaRepository.findAll();
        if (vagas.isEmpty()) {
            throw new BusinessRuleException("Nenhuma vaga encontrada.");
        }
        return vagas.stream()
                .map(this::listar)
                .collect(Collectors.toList());
    }

    public Vaga listarPorId(UUID id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Vaga", id));
        return vaga;
    }

    public List<VagaRespostaDto> listarTodasPorEmpresa(UUID empresaId) {
      List<Vaga> vagas = vagaRepository.findByEmpresaId(empresaId);

      // sem excessão porque nenhuma empresa já entra com vagas
      return vagas.stream()
        .map(this::listar)
        .collect(Collectors.toList());
    }

    public VagaRespostaDto atualizarVagaPorId(UUID id, VagaDto data) {
        validarPesos(data);
        Vaga vaga = vagaRepository.findById(id)
          .orElseThrow(() -> new ItemNotFoundException("Vaga", id));
        Empresa empresa = empresaRepository.findById(data.empresaId())
          .orElseThrow(() -> new BusinessRuleException("Empresa não encontrada"));
        vaga.vagaDtoMapper(data, empresa);
        Vaga vagaAtualizada = vagaRepository.save(vaga);
        return listar(vagaAtualizada);
    }

//    public VagaRespostaDto atualizarVagaPorId(UUID id, VagaDto data, Authentication authentication) {
//        validarPesos(data);
//
//        Vaga vaga = vagaRepository.findById(id)
//                .orElseThrow(() -> new ItemNotFoundException("Vaga", id));
//
//        String emailEmpresa = authentication.getName();
//        Empresa empresaLogada = empresaRepository.findByEmail(emailEmpresa)
//          .orElseThrow(() -> new BusinessRuleException("Empresa autenticada não encontrada"));
//
//        if (!vaga.getEmpresa().getId().equals(empresaLogada.getId())) {
//            throw new BusinessRuleException("Você não tem permissão para atualizar esta vaga.");
//        }
//        vaga.vagaDtoMapper(data, empresaLogada);
//        Vaga vagaAtualizada = vagaRepository.save(vaga);
//        return listar(vagaAtualizada);
//    }


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

    
}