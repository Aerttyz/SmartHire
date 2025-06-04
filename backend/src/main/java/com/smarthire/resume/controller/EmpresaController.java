package com.smarthire.resume.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smarthire.resume.domain.DTO.EmpresaPatchRequestDto;
import com.smarthire.resume.domain.DTO.EmpresaResponseDTO;
import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.service.EmpresaService;

import jakarta.validation.Valid;

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

    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>> listarTodasEmpresas() {
        return ResponseEntity.ok(empresaService.listarTodas());
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
