package com.smarthire.resume.service;

import com.smarthire.resume.domain.model.Candidato;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.CandidatoRepository;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import jakarta.transaction.Transactional;

import java.util.List;

public class VagaService {

    private VagaRepository vagaRepository;

    @Transactional
    public Vaga salvar(Vaga vaga) {
        return vagaRepository.save(vaga);
    }

    public List<Vaga> listar(Vaga vaga) {
        return vagaRepository.findAll();
    }

    public void excluir(Vaga vaga) {
        vagaRepository.save(vaga);
    }

}
