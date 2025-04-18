package com.smarthire.resume.controller;

import com.smarthire.resume.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/resume")
public class CurriculoController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/analyze-folder")
    public ResponseEntity<?> analyzeFolder(@RequestBody Map<String, String> requestBody) {
        String path = requestBody.get("path");

        Map<String, Object> result = resumeService.analyzeFolder(path);
        return ResponseEntity.ok(result);
    }
}
