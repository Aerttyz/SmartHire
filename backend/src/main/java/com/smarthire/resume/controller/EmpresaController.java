package com.smarthire.resume.controller;

import com.smarthire.resume.domain.DTO.EmpresaResponseDTO;
import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.DTO.EmpresaRequestDTO;
import com.smarthire.resume.service.EmpresaService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping("/me")
    public ResponseEntity<EmpresaResponseDTO> buscarEmpresa() {
        EmpresaResponseDTO empresa = empresaService.buscarEmpresa();
        return ResponseEntity.ok(empresa);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizarEmpresaPorId(@PathVariable UUID id,
    @Valid @RequestBody EmpresaRequestDTO data) {
        Empresa empresaAtualizada = empresaService.atualizarEmpresaPorId(id, data);
        return ResponseEntity.ok(empresaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerEmpresa(@PathVariable UUID id) {
        empresaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
