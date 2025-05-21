package com.smarthire.resume.controller;

import com.smarthire.resume.domain.DTO.CandidateScoreDTO;
import com.smarthire.resume.domain.DTO.FaseDto;
import com.smarthire.resume.domain.DTO.VagaDto;
import com.smarthire.resume.domain.DTO.VagaRespostaDto;
import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.EmpresaRepository;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.service.PontuacaoVagaService;
import com.smarthire.resume.service.VagaService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;


@AllArgsConstructor
@RestController
@RequestMapping("/vagas")
public class VagaController {
    @Autowired
    private VagaService vagaService;
    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private PontuacaoVagaService pontuacaoVagaService;

   @GetMapping
    public ResponseEntity<List<VagaRespostaDto>> listarTodas() {
        List<VagaRespostaDto> vagas = vagaService.listarTodas();
        return ResponseEntity.ok(vagas);
    }

    @GetMapping({"/{nomeVaga}"})
    public ResponseEntity<List<VagaRespostaDto>> buscarVaga(@PathVariable String nomeVaga) {
        List<VagaRespostaDto> vagaRespostaDto = vagaService.listarPorNome(nomeVaga);
        return ResponseEntity.ok(vagaRespostaDto);
    }

    
    @PostMapping
    public ResponseEntity<?> adicionarvaga(@Valid @RequestBody VagaDto vaga) {
        vagaService.salvar(vaga);
        return ResponseEntity.ok("Vaga cadastrada com sucesso");
    }

    @PostMapping("/{id}/fases")
    public ResponseEntity<?> adicionarFasesNaVaga(@PathVariable UUID id, 
                                                            @Valid @RequestBody List<FaseDto> fasesDto) {
        vagaService.cadastrarFase(id, fasesDto);
        return ResponseEntity.ok("Fases adicionadas com sucesso");
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<VagaRespostaDto> atualizarVagaPorId(@PathVariable UUID id,
                                                              @Valid @RequestBody VagaDto data) {
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

}
