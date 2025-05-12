package com.smarthire.resume.controller;

import com.smarthire.resume.domain.model.Candidato;
import com.smarthire.resume.domain.repository.CandidatoRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.service.CandidatoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/candidatos")
public class CandidatoController {

    @Autowired
    private CandidatoService candidatoService;
    @Autowired
    private CandidatoRepository candidatoRepository;

    @GetMapping
    public List<Candidato> listarCandidatos() {
        return candidatoRepository.findAll();
    }

    @GetMapping({"/{nomeCandidato}"})
    public ResponseEntity<Candidato> buscarCandidato(@PathVariable String nomeCandidato) {
        Optional<Candidato> candidatoOptional = candidatoRepository.findByNome(nomeCandidato);
        if (candidatoOptional.isPresent()) {
            return ResponseEntity.ok(candidatoOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Candidato adicionarcandidato(@Valid @RequestBody Candidato candidato) {
        return candidatoService.salvar(candidato);
    }
    
    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> removerCandidato(@PathVariable UUID id) {
        Optional<Candidato> candidatoOptional = candidatoRepository.findById(id);
        if (candidatoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        candidatoService.deletarCandidatoPorId(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<String> capturar(BusinessRuleException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
