package com.smarthire.resume.controller;

import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.repository.EmpresaRepository;
import com.smarthire.resume.domain.DTO.EmpresaRequestDTO;
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


@AllArgsConstructor
@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private EmpresaRepository empresaRepository;

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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Empresa adicionarEmpresa(@Valid @RequestBody Empresa empresa) {
        return empresaService.salvar(empresa);
    }

    // RETIRAR LOGICA DE NEGOCIO DO CONTROLLER --SAVIO
    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizarEmpresaPorId(@PathVariable UUID id,
                                                    @Valid @RequestBody EmpresaRequestDTO data) {
        Optional<Empresa> empresaOptional = empresaRepository.findById(id);
        if (empresaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Empresa empresa = empresaOptional.get();
        empresa.atualizarCom(data);
        return ResponseEntity.ok(empresaService.salvar(empresa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerEmpresa(@PathVariable UUID id) {
        empresaService.excluir(id);
        return ResponseEntity.noContent().build();
    }


}
