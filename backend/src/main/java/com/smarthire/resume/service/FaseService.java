package com.smarthire.resume.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smarthire.resume.domain.DTO.FaseDto;
import com.smarthire.resume.domain.model.Fase;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.FaseRepository;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.ItemNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class FaseService {

    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private FaseRepository faseRepository;

    @Transactional
    public void cadastrarFase(UUID id, List<FaseDto> fasesDto) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Vaga", id));

        List<Fase> fases = fasesDto.stream()
                .map(faseDto -> {
                    Fase fase = new Fase();
                    fase.setTitulo(faseDto.titulo());
                    fase.setDescricao(faseDto.descricao());
                    fase.setOrdem(faseDto.ordem());
                    fase.setVaga(vaga);
                    return fase;
                })
                .collect(Collectors.toList());
        faseRepository.saveAll(fases);
    }
}
