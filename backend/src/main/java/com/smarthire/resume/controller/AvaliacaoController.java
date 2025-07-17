package com.smarthire.resume.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smarthire.resume.domain.DTO.AvaliacaoDTO;
import com.smarthire.resume.service.AvaliacaoService;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping("/avaliar/{vagaId}/{candidatoId}")
    public ResponseEntity<AvaliacaoDTO> avaliacaoLLM(@PathVariable("vagaId") UUID vagaId,
            @PathVariable("candidatoId") UUID candidatoId) {
        return ResponseEntity.ok(avaliacaoService.realizarAvaliacao(vagaId, candidatoId));
    }

}
