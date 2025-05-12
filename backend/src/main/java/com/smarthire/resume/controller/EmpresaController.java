package com.smarthire.resume.controller;

import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.repository.EmpresaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.service.EmpresaService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@AllArgsConstructor
@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping
    public List<Empresa> listarEmpresas() {
        return empresaRepository.findAll();
    }

    @GetMapping({"/{nomeEmpresa}"})
    public ResponseEntity<Empresa> buscarEmpresa(@PathVariable String nomeEmpresa) {
        Optional<Empresa> empresaOptional = empresaRepository.findByNomeIgnoreCase(nomeEmpresa);
        return empresaOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Empresa adicionarEmpresa(@Valid @RequestBody Empresa empresa) {
        return empresaService.salvar(empresa);
    }

    // REFATORAR - SAVIO

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizarEmpresaPorNome(@PathVariable String nomeEmpresa,
                                                    @Valid @RequestBody Empresa empresa) {
        if (!empresaRepository.existsByNome(empresa.getNome())) {
            return ResponseEntity.notFound().build();
        }
        empresa.setNome(nomeEmpresa);

        return ResponseEntity.ok(empresa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerEmpresa(@PathVariable UUID id) {
        Optional<Empresa> empresaOptional = empresaRepository.findById(id);
        if (empresaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        empresaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<String> capturar(BusinessRuleException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
