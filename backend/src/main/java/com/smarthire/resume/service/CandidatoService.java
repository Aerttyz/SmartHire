package com.smarthire.resume.service;

import com.smarthire.resume.domain.model.Candidato;
import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.repository.CandidatoRepository;
import com.smarthire.resume.domain.repository.EmpresaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CandidatoService {
    private CandidatoRepository candidatoRepository;

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

    public List<Candidato> listar(Candidato candidato) {
        return candidatoRepository.findAll();
    }

    public void excluir(Candidato candidato) {
        candidatoRepository.save(candidato);
    }
}
