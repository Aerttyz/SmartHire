package com.smarthire.resume.controller;

import com.smarthire.resume.domain.model.AvaliacaoLLM;
import com.smarthire.resume.service.AvaliacaoLLMService;
import com.smarthire.resume.service.CurriculoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.smarthire.resume.domain.model.Curriculo;

@RestController
@RequestMapping("/curriculos")
public class CurriculoController {

    @Autowired
    private CurriculoService curriculoService;

    @Autowired
    private AvaliacaoLLMService avaliacaoService;

    @PostMapping("/analisar-curriculos/{idVaga}")
    public ResponseEntity<?> analisarCurriculos(@PathVariable("idVaga") UUID idVaga,
                                           @RequestBody Map<String, String> requestBody) {

        String path = requestBody.get("path");

        List<Curriculo>result = curriculoService.salvarCurriculo(path, idVaga);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/avaliar/{vagaId}/{curriculoId}")
    public ResponseEntity<?> avaliarFitCandidatoVaga(
      @PathVariable UUID vagaId,
      @PathVariable UUID curriculoId) {

        AvaliacaoLLM avaliacao = avaliacaoService.avaliarCandidatoParaVaga(vagaId, curriculoId);
        return ResponseEntity.ok(avaliacao);
    }

}
