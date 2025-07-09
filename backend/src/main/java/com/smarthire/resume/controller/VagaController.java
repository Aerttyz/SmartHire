package com.smarthire.resume.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smarthire.resume.domain.DTO.VagaDto;
import com.smarthire.resume.domain.DTO.VagaRespostaDto;
import com.smarthire.resume.service.AnaliseService;
import com.smarthire.resume.service.PontuacaoVagaService;
import com.smarthire.resume.service.VagaService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/vagas")
public class VagaController {

    @Autowired
    private VagaService vagaService;
    @Autowired
    private PontuacaoVagaService pontuacaoVagaService;
    @Autowired
    private AnaliseService analiseService;

    @GetMapping
    public ResponseEntity<List<VagaRespostaDto>> listarVagasDaEmpresa() {
        List<VagaRespostaDto> vagas = vagaService.listarTodasPorEmpresa();
        return ResponseEntity.ok(vagas);
    }

    @GetMapping({ "/{nomeVaga}" })
    public ResponseEntity<List<VagaRespostaDto>> buscarVaga(@PathVariable String nomeVaga) {
        List<VagaRespostaDto> vagaRespostaDto = vagaService.listarPorNome(nomeVaga);
        return ResponseEntity.ok(vagaRespostaDto);
    }

    @GetMapping({ "/id/{id}" })
    public ResponseEntity<VagaRespostaDto> buscarVagaPorId(@PathVariable UUID id) {
        VagaRespostaDto vagaRespostaDto = vagaService.listarVagaPorId(id);
        return ResponseEntity.ok(vagaRespostaDto);
    }

    @PostMapping
    public ResponseEntity<?> adicionarVaga(@Valid @RequestBody VagaDto vaga) {
        vagaService.salvar(vaga);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // @PatchMapping("/{id}")
    // public ResponseEntity<VagaRespostaDto> atualizarVagaPorId(@PathVariable UUID
    // id,
    // @Valid @RequestBody VagaPatchResposta data) {
    // VagaRespostaDto vagaAtualizada = vagaService.atualizarVagaPorId(id, data);
    // return ResponseEntity.ok(vagaAtualizada);
    // }

    @DeleteMapping({ "/{id}" })
    public ResponseEntity<Void> removerVaga(@PathVariable UUID id) {
        vagaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // endpoint adicionado nesse controller por haver apenas 1 operação de pontuação
    // de candidatos
    @PostMapping("/{idVaga}/pontuacoes")
    public ResponseEntity<String> obterPontuacoesCandidatos(@PathVariable UUID idVaga) {
        return ResponseEntity.ok(analiseService.realizarAnalise(idVaga));
    }

    @PostMapping("/{vagaId}/enviar-emails-inaptos")
    public ResponseEntity<String> enviarEmails(@PathVariable UUID vagaId) {
        pontuacaoVagaService.enviarEmailsParaCandidatosInaptos(vagaId);
        return ResponseEntity.ok("Emails enviados para candidatos com pontuação abaixo da mínima");
    }

}
