package com.smarthire.resume.controller;

import com.smarthire.resume.service.CurriculoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.smarthire.resume.domain.model.Curriculo;

@RestController
@RequestMapping("/resume")
public class CurriculoController {

    @Autowired
    private CurriculoService curriculoService;

    @PostMapping("/analisar-curriculos/{idVaga}")
    public ResponseEntity<?> analyzeFolder(@PathVariable("idVaga") UUID idVaga,
                                           @RequestBody Map<String, String> requestBody) {

        String path = requestBody.get("path");

        List<Curriculo>result = curriculoService.salvarCurriculo(path, idVaga);
        return ResponseEntity.ok(result);
    }
}
