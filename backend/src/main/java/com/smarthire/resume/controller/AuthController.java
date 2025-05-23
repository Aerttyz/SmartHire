package com.smarthire.resume.controller;

import java.util.Collections;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smarthire.resume.domain.DTO.AuthDto;
import com.smarthire.resume.service.AuthService;
import com.smarthire.resume.service.EmpresaService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDto authDto) {
        try {
            return ResponseEntity.ok(authService.login(authDto));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("error", "Email ou senha inválidos"));
        }
    }
}
