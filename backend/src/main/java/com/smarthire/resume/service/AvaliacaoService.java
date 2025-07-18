package com.smarthire.resume.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.smarthire.resume.domain.DTO.AvaliacaoDTO;
import com.smarthirepro.core.service.impl.AvaliacaoLlmImpl;

@Service
public class AvaliacaoService {
    private final AvaliacaoLlmImpl<AvaliacaoDTO> avaliacaoLlm;

    public AvaliacaoService(AvaliacaoLlmImpl<AvaliacaoDTO> avaliacaoLlm) {
        this.avaliacaoLlm = avaliacaoLlm;
    }

    public AvaliacaoDTO realizarAvaliacao(UUID vagaId, UUID candidatoId) {
        return avaliacaoLlm.realizarAvaliacao(vagaId, candidatoId);
    }
}
