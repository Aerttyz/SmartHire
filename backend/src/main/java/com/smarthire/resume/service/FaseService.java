package com.smarthire.resume.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smarthire.resume.domain.model.Candidato;
import com.smarthire.resume.domain.model.CandidatoFase;
import com.smarthire.resume.domain.model.Fase;
import com.smarthire.resume.domain.repository.CandidatoFaseRepository;
import com.smarthire.resume.domain.repository.CandidatoRepository;
import com.smarthire.resume.domain.repository.FaseRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.exception.ItemNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class FaseService {

    @Autowired
    private CandidatoFaseRepository candidatoFaseRepository;
    @Autowired
    private FaseRepository faseRepository;
    @Autowired
    private CandidatoRepository candidatoRepository;


    @Transactional
    public void adicionarCandidatoAFase(UUID idFase, List<UUID> idCandidatos) { 
        Fase fase = faseRepository.findById(idFase)
                .orElseThrow(() -> new ItemNotFoundException("Fase", idFase));

        List<Candidato> candidatos = candidatoRepository.findAllById(idCandidatos);

        Set<UUID> encontrados = candidatos.stream()
                .map(Candidato::getId)
                .collect(Collectors.toSet());

        List<UUID> naoEncontrados = idCandidatos.stream()
                .filter(id -> !encontrados.contains(id))
                .collect(Collectors.toList());

        if (!naoEncontrados.isEmpty()) {   
            throw new BusinessRuleException("Candidatos não encontrados: " + naoEncontrados);
        }
        
        List<CandidatoFase> candidatosNaFase = candidatoFaseRepository.findByCandidato_IdIn(idCandidatos);

        Map<UUID, CandidatoFase> candidatosNaFaseMap = candidatosNaFase.stream()
                .collect(Collectors.toMap(candidatoFase -> candidatoFase.getCandidato().getId(), candidatoFase -> candidatoFase));

        List<CandidatoFase> atualizarCandidatoNaFase = new ArrayList<>();

        for(Candidato candidato : candidatos) {
            CandidatoFase candidatoFase = candidatosNaFaseMap.get(candidato.getId());

            if (candidatoFase != null && candidatoFase.getFase().getId().equals(idFase)) {
            throw new BusinessRuleException("Candidato já está na fase: " + candidato.getId());
            }
            if (candidatoFase != null) {
                candidatoFase.setFase(fase);
                candidatoFase.setDataInicio(LocalDateTime.now());
                atualizarCandidatoNaFase.add(candidatoFase);
            } else {
                candidatoFase = new CandidatoFase();
                candidatoFase.setCandidato(candidato);
                candidatoFase.setFase(fase);
                candidatoFase.setDataInicio(LocalDateTime.now());
                atualizarCandidatoNaFase.add(candidatoFase);
            }
        }
        candidatoFaseRepository.saveAll(atualizarCandidatoNaFase);
    }
}
