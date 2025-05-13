package com.smarthire.resume.controller;

import com.smarthire.resume.domain.DTO.VagaDto;
import com.smarthire.resume.domain.DTO.VagaRespostaDto;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.service.VagaService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/vagas")
public class VagaController {
    @Autowired
    private VagaService vagaService;
    @Autowired
    private VagaRepository vagaRepository;

   @GetMapping
    public ResponseEntity<List<VagaRespostaDto>> listarTodas() {
        List<Vaga> vagas = vagaRepository.findAll();
        List<VagaRespostaDto> dtos = vagas.stream()
                .map(vagaService::listar)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping({"/{nomeVaga}"})
    public ResponseEntity<Vaga> buscarVaga(@PathVariable String nomeVaga) {
        Optional<Vaga> vagaOptional = vagaRepository.findByNome(nomeVaga);
        if (vagaOptional.isPresent()) {
            return ResponseEntity.ok(vagaOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> adicionarvaga(@Valid @RequestBody VagaDto vaga) {
        
        vagaService.salvar(vaga);
        return ResponseEntity.ok("Vaga cadastrada com sucesso");
    }


    // REFATORAR - SAVIO
    public ResponseEntity<Vaga> atualizarvagaPorNome(@PathVariable String nomeVaga,
                                                               @Valid @RequestBody Vaga vaga) {
        if (!vagaRepository.existsByNome(vaga.getNome())) {
            return ResponseEntity.notFound().build();
        }
        vaga.setNome(nomeVaga);

        return ResponseEntity.ok(vaga);
    }

    
    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> removerVaga(@PathVariable UUID id) {
        Optional<Vaga> vagaOptional = vagaRepository.findById(id);
        if (vagaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        vagaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<String> capturar(BusinessRuleException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
