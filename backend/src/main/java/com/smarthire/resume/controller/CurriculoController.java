package com.smarthire.resume.controller;

import com.smarthire.resume.service.CurriculoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import com.smarthire.resume.domain.model.Curriculo;

@RestController
@RequestMapping("/curriculos")
public class CurriculoController {

    @Autowired
    private CurriculoService curriculoService;

    @PostMapping("/analisar-curriculos/{idVaga}")
    public ResponseEntity<?> analisarCurriculos(@PathVariable("idVaga") UUID idVaga,
                                           @RequestParam("file") MultipartFile file) throws Exception {
        
        String path = curriculoService.pegarCaminhoDoCurriculo(file, idVaga);

        List<Curriculo>result = curriculoService.salvarCurriculo(path, idVaga);
        return ResponseEntity.ok(result);
    }
}
