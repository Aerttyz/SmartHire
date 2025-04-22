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

    /*
        Caso de uso:
        -   Empresa deseja buscar empresas clientes dentro da plataforma
     */
    @GetMapping({"/{nomeEmpresa}"})
    public ResponseEntity<Empresa> buscarEmpresa(@PathVariable String nomeEmpresa) {
        Optional<Empresa> empresaOptional = empresaRepository.findByNome(nomeEmpresa);
        if (empresaOptional.isPresent()) {
            return ResponseEntity.ok(empresaOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Empresa adicionarEmpresa(@Valid @RequestBody Empresa empresa) {
        return empresaService.salvar(empresa);
    }

    public ResponseEntity<Empresa> atualizarEmpresaPorNome(@PathVariable String nomeEmpresa,
                                                    @Valid @RequestBody Empresa empresa) {
        if (!empresaRepository.existsByNome(empresa.getNome())) {
            return ResponseEntity.notFound().build();
        }
        empresa.setNome(nomeEmpresa);

        return ResponseEntity.ok(empresa);
    }

    /* Caso de uso
        - Empresa deseja excluir cadastro
    */
    @DeleteMapping
    public ResponseEntity<Void> removerEmpresa(@PathVariable String nomeEmpresa) {
        if (!empresaRepository.existsByNome(nomeEmpresa)) {
            return ResponseEntity.notFound().build();
        }
        empresaService.excluir(nomeEmpresa);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<String> capturar(BusinessRuleException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
