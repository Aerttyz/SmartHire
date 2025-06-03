package com.smarthire.resume.controller;

import com.smarthire.resume.domain.DTO.EmpresaResponseDTO;
import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.service.EmpresaService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import com.smarthire.resume.domain.DTO.EmpresaPatchRequestDto;

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
    
    @PatchMapping
    public ResponseEntity<Empresa> atualizarEmpresaPorId(@Valid @RequestBody EmpresaPatchRequestDto data) {
        Empresa empresaAtualizada = empresaService.atualizarEmpresaPorId(data);
        return ResponseEntity.ok(empresaAtualizada);
    }

    @DeleteMapping
    public ResponseEntity<Void> removerEmpresa() {
        empresaService.excluir();
        return ResponseEntity.noContent().build();
    }
}
