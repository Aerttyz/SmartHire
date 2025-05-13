package com.smarthire.resume.service;

import com.smarthire.resume.domain.DTO.VagaDto;
import com.smarthire.resume.domain.DTO.VagaRequisitosDto;
import com.smarthire.resume.domain.DTO.VagaRespostaDto;
import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.model.VagaRequisitosModel;
import com.smarthire.resume.domain.repository.EmpresaRepository;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private EmpresaRepository empresaRepository;

    @Transactional
    public void salvar(VagaDto dto) {
        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new BusinessRuleException("Empresa não encontrada."));
        Vaga vaga = new Vaga();
        vaga.setNome(dto.getNome());
        vaga.setEmpresa(empresa);
        vaga.setActive(dto.isActive());

        VagaRequisitosModel requisitos = new VagaRequisitosModel();
        requisitos.setHabilidades(dto.getHabilidades());
        requisitos.setExperiencia(dto.getExperiencia());
        requisitos.setFormacaoAcademica(dto.getFormacaoAcademica());
        requisitos.setIdiomas(dto.getIdiomas());
        requisitos.setPesoHabilidades(dto.getPesoHabilidades());
        requisitos.setPesoIdiomas(dto.getPesoIdiomas());
        requisitos.setPesoFormacaoAcademica(dto.getPesoFormacaoAcademica());
        requisitos.setPesoExperiencia(dto.getPesoExperiencia());

        requisitos.setVaga(vaga);
        vaga.setRequisitos(requisitos);

        vagaRepository.save(vaga);
    }

    public VagaRespostaDto listar(Vaga vaga) {
        VagaRespostaDto vagaRespostaDto = new VagaRespostaDto();
        vagaRespostaDto.setId(vaga.getId());
        vagaRespostaDto.setNome(vaga.getNome());
        vagaRespostaDto.setActive(vaga.isActive());
        vagaRespostaDto.setEmpresaNome(vaga.getEmpresa().getNome());
        
        if (vaga.getRequisitos() != null) {
            VagaRequisitosModel requisitos = vaga.getRequisitos();
            
            VagaRequisitosDto requisitosDto = new VagaRequisitosDto();
            requisitosDto.setHabilidades(requisitos.getHabilidades());
            requisitosDto.setExperiencia(requisitos.getExperiencia());
            requisitosDto.setFormacaoAcademica(requisitos.getFormacaoAcademica());
            requisitosDto.setIdiomas(requisitos.getIdiomas());
            requisitosDto.setPesoHabilidades(requisitos.getPesoHabilidades());
            requisitosDto.setPesoIdiomas(requisitos.getPesoIdiomas());
            requisitosDto.setPesoFormacaoAcademica(requisitos.getPesoFormacaoAcademica());
            requisitosDto.setPesoExperiencia(requisitos.getPesoExperiencia());

            vagaRespostaDto.setRequisitos(requisitosDto);
        }
        return vagaRespostaDto;
    }

    public void excluir(UUID id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Vaga não encontrada."));
        vagaRepository.delete(vaga);
    }

}
