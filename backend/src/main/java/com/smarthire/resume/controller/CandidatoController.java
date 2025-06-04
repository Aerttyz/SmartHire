package com.smarthire.resume.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smarthire.resume.domain.DTO.CandidatoDto;
import com.smarthire.resume.domain.DTO.CandidatoRequestDTO;
import com.smarthire.resume.domain.DTO.EmailDTO;
import com.smarthire.resume.domain.model.Candidato;
import com.smarthire.resume.service.CandidatoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/candidatos")
public class CandidatoController {

    @Autowired
    private CandidatoService candidatoService;

    @GetMapping
    public ResponseEntity<List<CandidatoDto>> listar() {
        List<CandidatoDto> candidatos = candidatoService.listarTodos();
        return ResponseEntity.ok(candidatos);
    }

    @GetMapping("/me")
    public ResponseEntity<Integer> listarNumeroCandidatosEmpresaLogada() {
        List<CandidatoDto> candidatos = candidatoService.listarTodosPorEmpresaId();
        int numeroCandidatos = candidatos.size();
        return ResponseEntity.ok(numeroCandidatos);
    }

    @GetMapping("/me/listar")
    public ResponseEntity<List<CandidatoDto>> listarCandidatosEmpresaLogada() {
        List<CandidatoDto> candidatos = candidatoService.listarTodosPorEmpresaId();
        return ResponseEntity.ok(candidatos);
    }

    @GetMapping({ "/{nomeCandidato}" })
    public ResponseEntity<List<CandidatoDto>> buscarCandidato(@PathVariable String nomeCandidato) {
        List<CandidatoDto> candidatos = candidatoService.buscarCandidatoPorNome(nomeCandidato);
        return ResponseEntity.ok(candidatos);
    }

    @PostMapping("/{idCandidato}/vaga/{idVaga}")
    public ResponseEntity<Void> adicionarCandidatoAVaga(@PathVariable UUID idCandidato, @PathVariable UUID idVaga) {
        candidatoService.adicionarCandidatoAVaga(idCandidato, idVaga);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidato> atualizarPeloId(
            @PathVariable UUID id,
            @Valid @RequestBody CandidatoRequestDTO data) {

        Candidato candidatoAtualizado = candidatoService.atualizarCandidatoPorId(id, data);
        return ResponseEntity.ok(candidatoAtualizado);
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<Candidato> atualizarEmail(@PathVariable UUID id,
            @Valid @RequestBody EmailDTO data) {
        Candidato candidatoAtualizado = candidatoService.atualizarEmailPorId(id, data);
        return ResponseEntity.ok(candidatoAtualizado);
    }

    @DeleteMapping({ "/{id}" })
    public ResponseEntity<Void> removerCandidato(@PathVariable UUID id) {
        candidatoService.deletarCandidatoPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/media")
    public ResponseEntity<Double> obterMediaCandidatoVaga() {
        Double media = candidatoService.obterMediaCandidatoVaga();
        return ResponseEntity.ok(media);
    }

}
