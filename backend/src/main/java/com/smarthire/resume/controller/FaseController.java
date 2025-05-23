package com.smarthire.resume.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smarthire.resume.domain.DTO.FaseDto;
import com.smarthire.resume.domain.model.Candidato;
import com.smarthire.resume.service.FaseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/fases")
public class FaseController {

    @Autowired
    private FaseService faseService;


     @PostMapping("/{idFase}/candidato")
    public ResponseEntity<Candidato> adicionarCandidatoAFase(@PathVariable UUID idFase, @Valid @RequestBody List<UUID> candidatosIds) {
        faseService.adicionarCandidatoAFase(idFase, candidatosIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/fases")
    public ResponseEntity<?> adicionarFasesNaVaga(@PathVariable UUID id, 
                                                            @Valid @RequestBody List<FaseDto> fasesDto) {
        faseService.cadastrarFase(id, fasesDto);
        return ResponseEntity.ok("Fases adicionadas com sucesso");
    }
}
