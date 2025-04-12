package com.smarthire.resume.controllers;

import com.smarthire.resume.services.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/analyze-folder")
    public ResponseEntity<?> analyzeFolder(@RequestBody Map<String, String> requestBody) {
        String path = requestBody.get("path");

        if (path == null || path.isEmpty()) {
            return ResponseEntity.badRequest().body("Path is required");
        }

        Map<String, Object> result = resumeService.analyzeFolder(path);
        return ResponseEntity.ok(result);
    }
}
