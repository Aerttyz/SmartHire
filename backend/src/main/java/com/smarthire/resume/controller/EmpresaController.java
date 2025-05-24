package com.smarthire.resume.controller;

import com.smarthire.resume.domain.DTO.EmpresaResponseDTO;
import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.DTO.EmpresaRequestDTO;
import com.smarthire.resume.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public ResponseEntity<List<Empresa>> listarEmpresas() {
        List<Empresa> empresas = empresaService.listarTodas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping({"/{nomeEmpresa}"})
    public ResponseEntity<List<Empresa>> buscarEmpresa(@PathVariable String nomeEmpresa) {
        List<Empresa> empresas = empresaService.listarPorNome(nomeEmpresa);
        return ResponseEntity.ok(empresas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizarEmpresaPorId(@PathVariable UUID id,
                                                         @Valid @RequestBody EmpresaRequestDTO data) {
        Empresa empresaAtualizada = empresaService.atualizarEmpresaPorId(id, data);
        return ResponseEntity.ok(empresaAtualizada);
    }

    @PutMapping("/me")
    public ResponseEntity<EmpresaResponseDTO> atualizarEmpresaPorIdEncapsulado(@Valid @RequestBody EmpresaRequestDTO data,
                                                                    Authentication authentication) {
        EmpresaResponseDTO empresaAtualizada = empresaService.atualizarEmpresaPorIdEncapsulado(data, authentication);
        return ResponseEntity.ok(empresaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerEmpresa(@PathVariable UUID id) {
        empresaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> removerEmpresaPorEmail(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        empresaService.excluirPorEmail(email);
        return ResponseEntity.noContent().build();
    }


}
