package com.smarthire.resume.controller;

import com.smarthire.resume.domain.enums.Situacao;
import com.smarthire.resume.domain.DTO.CandidatoDto;
import com.smarthire.resume.domain.model.Candidato;
import com.smarthire.resume.domain.model.Curriculo;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.CandidatoRepository;
import com.smarthire.resume.domain.repository.CurriculoRepository;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.domain.DTO.CandidatoRequestDTO;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.service.CandidatoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/candidatos")
public class CandidatoController {

    @Autowired
    private CurriculoRepository curriculoRepository;
    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private CandidatoService candidatoService;
    @Autowired
    private CandidatoRepository candidatoRepository;


    @GetMapping
    public ResponseEntity<List<CandidatoDto>> listar() {
        List<CandidatoDto> candidatos = candidatoService.listarTodos();
        return ResponseEntity.ok(candidatos);
    }

    @GetMapping({"/{nomeCandidato}"})
    public ResponseEntity<List<CandidatoDto>> buscarCandidato(@PathVariable String nomeCandidato) {
        List<CandidatoDto> candidatos = candidatoService.buscarCandidatoPorNome(nomeCandidato);
        return ResponseEntity.ok(candidatos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidato> atualizarPeloId(
            @PathVariable UUID id,
            @Valid @RequestBody CandidatoRequestDTO data) {

        Candidato candidatoAtualizado = candidatoService.atualizarCandidatoPorId(id, data);
        return ResponseEntity.ok(candidatoAtualizado);
    }

    // CONFIRMAR SE É NECESSÁRIO, SE NÃO RETIRAR 
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Candidato adicionarcandidato(@Valid @RequestBody Candidato candidato) {
        return candidatoService.salvar(candidato);
    }

    @PostMapping("/{idCandidato}/vaga/{idVaga}")
    public ResponseEntity<Void> adicionarCandidatoAVaga(@PathVariable UUID idCandidato, @PathVariable UUID idVaga) {
        candidatoService.adicionarCandidatoAVaga(idCandidato, idVaga);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> removerCandidato(@PathVariable UUID id) {
        candidatoService.deletarCandidatoPorId(id);
        return ResponseEntity.noContent().build();
    }

}
