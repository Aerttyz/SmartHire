package com.smarthire.resume.service;

import com.smarthire.resume.domain.model.Candidato;
import com.smarthire.resume.domain.repository.CandidatoRepository;
import com.smarthire.resume.exception.BusinessRuleException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public List<Candidato> listarCandidatos() {
        try {
            List<Candidato> candidatos = candidatoRepository.findAll();
            if (candidatos.isEmpty()) {
                throw new BusinessRuleException("Nenhum candidato encontrado.");
            }
        } catch (Exception e) {
            throw new BusinessRuleException("Erro ao listar candidatos: " + e.getMessage());
        }
        return candidatoRepository.findAll();
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
}
