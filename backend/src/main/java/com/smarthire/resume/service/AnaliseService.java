package com.smarthire.resume.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.smarthirepro.core.service.impl.AnaliseTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnaliseService {

    private final AnaliseTemplate analise;

    public String realizarAnalise(UUID id) {

        return analise.runAnalise(id);
    }
}
