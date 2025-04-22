package com.smarthire.resume.controller;

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

@AllArgsConstructor
@RestController
@RequestMapping("/vagas")
public class VagaController {
    @Autowired
    private VagaService vagaService;
    @Autowired
    private VagaRepository vagaRepository;

    @GetMapping
    public List<Vaga> listarVagas() {
        return vagaRepository.findAll();
    }

    /*
        Caso de uso:
        -   empresa deseja buscar vagas cadastradas
     */
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

    public ResponseEntity<Vaga> atualizarvagaPorNome(@PathVariable String nomeVaga,
                                                               @Valid @RequestBody Vaga vaga) {
        if (!vagaRepository.existsByNome(vaga.getNome())) {
            return ResponseEntity.notFound().build();
        }
        vaga.setNome(nomeVaga);

        return ResponseEntity.ok(vaga);
    }

    /* Caso de uso
        - empresa deseja excluir vaga de seu processo
    */
    @DeleteMapping
    public ResponseEntity<Void> removerVaga(@PathVariable String nomeVaga) {
        if (!vagaRepository.existsByNome(nomeVaga)) {
            return ResponseEntity.notFound().build();
        }
        vagaService.excluir(nomeVaga);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<String> capturar(BusinessRuleException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
