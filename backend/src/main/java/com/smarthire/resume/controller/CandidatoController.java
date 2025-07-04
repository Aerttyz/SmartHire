package com.smarthire.resume.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smarthirepro.core.service.impl.CandidatoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/candidatos")
public class CandidatoController {

    @Autowired
    private CandidatoService candidatoService;

    // @GetMapping
    // public ResponseEntity<List<CandidatoDto>> listar() {
    // List<CandidatoDto> candidatos = candidatoService.listarTodos();
    // return ResponseEntity.ok(candidatos);
    // }

    // @GetMapping("/me")
    // public ResponseEntity<Integer> listarNumeroCandidatosEmpresaLogada() {
    // List<CandidatoDto> candidatos = candidatoService.listarTodosPorEmpresaId();
    // int numeroCandidatos = candidatos.size();
    // return ResponseEntity.ok(numeroCandidatos);
    // }

    // @GetMapping("/me/listar")
    // public ResponseEntity<List<CandidatoDto>> listarCandidatosEmpresaLogada() {
    // List<CandidatoDto> candidatos = candidatoService.listarTodosPorEmpresaId();
    // return ResponseEntity.ok(candidatos);
    // }

    // @GetMapping({ "/{nomeCandidato}" })
    // public ResponseEntity<List<CandidatoDto>> buscarCandidato(@PathVariable
    // String nomeCandidato) {
    // List<CandidatoDto> candidatos =
    // candidatoService.buscarCandidatoPorNome(nomeCandidato);
    // return ResponseEntity.ok(candidatos);
    // }

    // @PatchMapping("/{id}/email")
    // public ResponseEntity<Candidato> atualizarEmail(@PathVariable UUID id,
    // @Valid @RequestBody EmailDTO data) {
    // Candidato candidatoAtualizado = candidatoService.atualizarEmailPorId(id,
    // data);
    // return ResponseEntity.ok(candidatoAtualizado);
    // }

    // @DeleteMapping({ "/{id}" })
    // public ResponseEntity<Void> removerCandidato(@PathVariable UUID id) {
    // candidatoService.deletarCandidatoPorId(id);
    // return ResponseEntity.noContent().build();
    // }

    // @GetMapping("/me/media")
    // public ResponseEntity<Double> obterMediaCandidatoVaga() {
    // Double media = candidatoService.obterMediaCandidatoVaga();
    // return ResponseEntity.ok(media);
    // }

}
