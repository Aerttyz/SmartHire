package com.smarthire.resume.controller;

import com.smarthire.resume.domain.DTO.VagaDto;
import com.smarthire.resume.domain.DTO.VagaRespostaDto;
import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.EmpresaRepository;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.service.VagaService;
import com.smarthire.resume.domain.DTO.VagaRequestDTO;

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
    @Autowired
    private EmpresaRepository empresaRepository;

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
    public Vaga adicionarvaga(@Valid @RequestBody Vaga vaga) {
        return vagaService.salvar(vaga);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Vaga> atualizarvagaPorId(@PathVariable UUID id,
                                                               @Valid @RequestBody VagaRequestDTO data) {
        Optional<Vaga> vagaOptional = vagaRepository.findById(id);
        if(vagaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Empresa empresaVaga = empresaRepository.findById(data.empresaId())
                .orElseThrow(() -> new BusinessRuleException("Empresa não encontrada"));

        Vaga vaga = vagaOptional.get();
        vaga.atualizarCom(data, empresaVaga);
        return ResponseEntity.ok(vagaService.salvar(vaga));
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
