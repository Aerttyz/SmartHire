package com.smarthire.resume.controller;

import com.smarthire.resume.domain.DTO.CandidateScoreDTO;
import com.smarthire.resume.domain.DTO.VagaDto;
import com.smarthire.resume.domain.DTO.VagaRespostaDto;
import com.smarthire.resume.service.PontuacaoVagaService;
import com.smarthire.resume.service.VagaService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import com.smarthire.resume.domain.DTO.VagaPatchResposta;

@AllArgsConstructor
@RestController
@RequestMapping("/vagas")
public class VagaController {

    @Autowired
    private VagaService vagaService;
    @Autowired
    private PontuacaoVagaService pontuacaoVagaService;

    @GetMapping
    public ResponseEntity<List<VagaRespostaDto>> listarVagasDaEmpresa() {
      List<VagaRespostaDto> vagas = vagaService.listarTodasPorEmpresa();
      return ResponseEntity.ok(vagas);
    }

    @GetMapping({"/{nomeVaga}"})
    public ResponseEntity<List<VagaRespostaDto>> buscarVaga(@PathVariable String nomeVaga) {
        List<VagaRespostaDto> vagaRespostaDto = vagaService.listarPorNome(nomeVaga);
        return ResponseEntity.ok(vagaRespostaDto);
    }

    @GetMapping({"/id/{id}"})
    public ResponseEntity<VagaRespostaDto> buscarVagaPorId(@PathVariable UUID id) {
        VagaRespostaDto vagaRespostaDto = vagaService.listarVagaPorId(id);
        return ResponseEntity.ok(vagaRespostaDto);
    }

    @PostMapping
    public ResponseEntity<?> adicionarVaga(@Valid @RequestBody VagaDto vaga) {
        vagaService.salvar(vaga);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VagaRespostaDto> atualizarVagaPorId(@PathVariable UUID id,
                                                            @Valid @RequestBody VagaPatchResposta data) {
    VagaRespostaDto vagaAtualizada = vagaService.atualizarVagaPorId(id, data);
    return ResponseEntity.ok(vagaAtualizada);
  }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> removerVaga(@PathVariable UUID id) {
        vagaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // endpoint adicionado nesse controller por haver apenas 1 operação de pontuação de candidatos
    @GetMapping("/{idVaga}/pontuacoes")
    public ResponseEntity<List<CandidateScoreDTO>> obterPontuacoesCandidatos(@PathVariable UUID idVaga) {
        List<CandidateScoreDTO> pontuacoes = pontuacaoVagaService.obterPontuacoesDeCandidatos(idVaga);
        return ResponseEntity.ok(pontuacoes);
    }

    @PostMapping("/{vagaId}/enviar-emails-inaptos")
    public ResponseEntity<String> enviarEmails(@PathVariable UUID vagaId) {
        pontuacaoVagaService.enviarEmailsParaCandidatosInaptos(vagaId);
        return ResponseEntity.ok("Emails enviados para candidatos com pontuação abaixo da mínima");
    }

}
