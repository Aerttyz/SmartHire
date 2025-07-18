package com.smarthire.resume.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smarthire.resume.domain.DTO.CandidatoDto;
import com.smarthire.resume.service.CandidatosService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/candidatos")
@RequiredArgsConstructor
public class CandidatoController {

    private final CandidatosService candidatosService;

    @GetMapping
    public ResponseEntity<List<CandidatoDto>> listar() {
        List<CandidatoDto> candidatos = candidatosService.listarTodos();
        return ResponseEntity.ok(candidatos);
    }

    @GetMapping("/me/media")
    public ResponseEntity<Double> obterMediaCandidatoVaga() {
        Double media = candidatosService.obterMediaCandidatoVaga();
        return ResponseEntity.ok(media);
    }

    @GetMapping("/me")
    public ResponseEntity<Integer> listarNumeroCandidatosEmpresaLogada() {
        List<CandidatoDto> candidatos = candidatosService.listarTodosPorEmpresaId();
        int numeroCandidatos = candidatos.size();
        return ResponseEntity.ok(numeroCandidatos);
    }

    @GetMapping("/me/listar")
    public ResponseEntity<List<CandidatoDto>> listarCandidatosEmpresaLogada() {
        List<CandidatoDto> candidatos = candidatosService.listarTodosPorEmpresaId();
        return ResponseEntity.ok(candidatos);
    }
}
